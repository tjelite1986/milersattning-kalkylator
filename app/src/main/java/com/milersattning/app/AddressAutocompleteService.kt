package com.milersattning.app

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.*
import java.io.IOException
import java.net.URLEncoder

data class AddressSuggestion(
    val displayName: String,
    val shortName: String
)

data class NominatimSuggestion(
    @SerializedName("display_name") val displayName: String,
    @SerializedName("name") val name: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("class") val className: String?,
    @SerializedName("address") val address: NominatimAddress?,
    @SerializedName("importance") val importance: Double?
)

data class NominatimAddress(
    @SerializedName("road") val road: String?,
    @SerializedName("house_number") val houseNumber: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("town") val town: String?,
    @SerializedName("village") val village: String?,
    @SerializedName("municipality") val municipality: String?,
    @SerializedName("county") val county: String?,
    @SerializedName("postcode") val postcode: String?
)

class AddressAutocompleteService {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("User-Agent", "Milersattning-Android-App/1.0")
                .build()
            chain.proceed(request)
        }
        .build()
    private val gson = Gson()
    
    companion object {
        private const val TAG = "AddressAutocomplete"
        private const val NOMINATIM_SEARCH_URL = "https://nominatim.openstreetmap.org/search"
    }
    
    fun searchAddresses(
        query: String,
        callback: (List<AddressSuggestion>) -> Unit
    ) {
        if (query.length < 2) {
            callback(emptyList())
            return
        }
        
        val encodedQuery = URLEncoder.encode(query, "UTF-8")
        // Förbättrade parametrar för bättre svenska adressförslag
        val url = "$NOMINATIM_SEARCH_URL?q=$encodedQuery&format=json&limit=5&countrycodes=se&addressdetails=1&extratags=1&dedupe=1&viewbox=10.03,55.30,24.18,69.06&bounded=1"
        
        val request = Request.Builder()
            .url(url)
            .build()
        
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Autocomplete network error: ${e.message}")
                callback(emptyList())
            }
            
            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseBody = response.body?.string()
                    if (responseBody == null) {
                        callback(emptyList())
                        return
                    }
                    
                    Log.d(TAG, "Autocomplete response: $responseBody")
                    
                    val results = gson.fromJson(responseBody, Array<NominatimSuggestion>::class.java)
                    
                    val suggestions = results
                        .sortedByDescending { it.importance ?: 0.0 } // Sortera efter viktighet
                        .filter { isRelevantResult(it) } // Filtrera bort irrelevanta resultat
                        .mapNotNull { result ->
                            val shortName = formatBetterShortName(result, query)
                            val displayName = formatBetterDisplayName(result)
                            
                            if (shortName.isNotBlank() && displayName.isNotBlank()) {
                                AddressSuggestion(displayName, shortName)
                            } else null
                        }
                        .distinctBy { it.displayName } // Ta bort dubbletter baserat på displayName istället
                    
                    callback(suggestions)
                    
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing autocomplete response: ${e.message}")
                    callback(emptyList())
                }
            }
        })
    }
    
    private fun isRelevantResult(result: NominatimSuggestion): Boolean {
        val className = result.className ?: ""
        val type = result.type ?: ""
        
        // Filtrera bort irrelevanta resultat
        return when {
            className in listOf("place", "highway", "amenity", "building") -> true
            type in listOf("city", "town", "village", "municipality", "administrative", "residential", "primary", "secondary", "trunk") -> true
            else -> false
        }
    }
    
    private fun formatBetterShortName(result: NominatimSuggestion, originalQuery: String): String {
        val address = result.address
        val name = result.name ?: ""
        
        // Kolla om ursprungsfrågan innehåller siffror (husnummer)
        val queryHasNumber = originalQuery.any { it.isDigit() }
        
        return when {
            // För vägar med husnummer
            address?.road != null && address.houseNumber != null -> {
                "${address.road} ${address.houseNumber}"
            }
            // För vägar utan husnummer - men om originalfrågan hade nummer, försök behålla det
            address?.road != null -> {
                if (queryHasNumber) {
                    // Extrahera husnummer från originalfrågan om möjligt
                    val numberMatch = Regex("\\b\\d+[a-zA-Z]?\\b").find(originalQuery)
                    if (numberMatch != null) {
                        "${address.road} ${numberMatch.value}"
                    } else {
                        address.road
                    }
                } else {
                    address.road
                }
            }
            // För städer/orter
            address?.city != null -> address.city
            address?.town != null -> address.town
            address?.village != null -> address.village
            address?.municipality != null -> address.municipality
            // Fallback till name eller första delen av display_name
            name.isNotBlank() && name.length < 40 -> name
            else -> result.displayName.split(",").first().trim().take(40)
        }
    }
    
    private fun formatBetterDisplayName(result: NominatimSuggestion): String {
        val address = result.address
        val parts = mutableListOf<String>()
        
        // Bygg upp en mer strukturerad display-adress
        when {
            address?.road != null -> {
                if (address.houseNumber != null) {
                    parts.add("${address.road} ${address.houseNumber}")
                } else {
                    parts.add(address.road)
                }
                
                // Lägg till stad/ort
                val city = address.city ?: address.town ?: address.village ?: address.municipality
                if (city != null) {
                    parts.add(city)
                }
                
                // Lägg till län om det finns och är användbart
                if (address.county != null && !address.county.contains("kommun")) {
                    parts.add(address.county)
                }
            }
            else -> {
                // För städer/orter utan specifik väg
                val city = address?.city ?: address?.town ?: address?.village ?: address?.municipality ?: result.name
                if (city != null) {
                    parts.add(city)
                    
                    if (address?.county != null && !address.county.contains("kommun")) {
                        parts.add(address.county)
                    }
                }
            }
        }
        
        return if (parts.isNotEmpty()) {
            parts.joinToString(", ").take(80)
        } else {
            // Fallback
            result.displayName.replace(", Sverige", "").take(80)
        }
    }
}