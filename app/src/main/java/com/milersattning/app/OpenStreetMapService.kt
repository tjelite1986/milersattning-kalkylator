package com.milersattning.app

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.*
import java.io.IOException
import java.net.URLEncoder

// Nominatim API responses (för adresssökning)
data class NominatimResult(
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lon: String,
    @SerializedName("display_name") val displayName: String
)

// OSRM API responses (för routing)
data class OSRMResponse(
    @SerializedName("routes") val routes: List<OSRMRoute>,
    @SerializedName("code") val code: String
)

data class OSRMRoute(
    @SerializedName("distance") val distance: Double, // i meter
    @SerializedName("duration") val duration: Double, // i sekunder
    @SerializedName("geometry") val geometry: String?
)

data class OSMDistanceResult(
    val success: Boolean,
    val distanceKm: Double = 0.0,
    val distanceText: String = "",
    val durationText: String = "",
    val errorMessage: String = ""
)

class OpenStreetMapService {
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
        private const val TAG = "OpenStreetMapService"
        private const val NOMINATIM_URL = "https://nominatim.openstreetmap.org/search"
        private const val OSRM_URL = "https://router.project-osrm.org/route/v1/driving"
    }
    
    fun calculateDistance(
        originAddress: String,
        destinationAddress: String,
        callback: (OSMDistanceResult) -> Unit
    ) {
        // Steg 1: Hitta koordinater för ursprungsadressen
        geocodeAddress(originAddress) { originResult ->
            if (!originResult.success) {
                callback(OSMDistanceResult(
                    success = false,
                    errorMessage = "Kunde inte hitta ursprungsadressen: ${originResult.errorMessage}"
                ))
                return@geocodeAddress
            }
            
            // Steg 2: Hitta koordinater för måladressen
            geocodeAddress(destinationAddress) { destResult ->
                if (!destResult.success) {
                    callback(OSMDistanceResult(
                        success = false,
                        errorMessage = "Kunde inte hitta måladressen: ${destResult.errorMessage}"
                    ))
                    return@geocodeAddress
                }
                
                // Steg 3: Beräkna rutt mellan koordinaterna
                calculateRoute(
                    originResult.lat, originResult.lon,
                    destResult.lat, destResult.lon,
                    callback
                )
            }
        }
    }
    
    private data class GeocodeResult(
        val success: Boolean,
        val lat: Double = 0.0,
        val lon: Double = 0.0,
        val errorMessage: String = ""
    )
    
    private fun geocodeAddress(address: String, callback: (GeocodeResult) -> Unit) {
        val encodedAddress = URLEncoder.encode(address, "UTF-8")
        val url = "$NOMINATIM_URL?q=$encodedAddress&format=json&limit=1&countrycodes=se&addressdetails=1"
        
        val request = Request.Builder()
            .url(url)
            .build()
        
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Geocoding network error: ${e.message}")
                callback(GeocodeResult(
                    success = false,
                    errorMessage = "Nätverksfel vid adresssökning: ${e.message}"
                ))
            }
            
            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseBody = response.body?.string()
                    if (responseBody == null) {
                        callback(GeocodeResult(
                            success = false,
                            errorMessage = "Tom respons från adresssökning"
                        ))
                        return
                    }
                    
                    Log.d(TAG, "Geocoding response: $responseBody")
                    
                    val results = gson.fromJson(responseBody, Array<NominatimResult>::class.java)
                    
                    if (results.isEmpty()) {
                        callback(GeocodeResult(
                            success = false,
                            errorMessage = "Adressen kunde inte hittas"
                        ))
                        return
                    }
                    
                    val result = results[0]
                    callback(GeocodeResult(
                        success = true,
                        lat = result.lat.toDouble(),
                        lon = result.lon.toDouble()
                    ))
                    
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing geocoding response: ${e.message}")
                    callback(GeocodeResult(
                        success = false,
                        errorMessage = "Fel vid tolkning av adresssvar: ${e.message}"
                    ))
                }
            }
        })
    }
    
    private fun calculateRoute(
        originLat: Double, originLon: Double,
        destLat: Double, destLon: Double,
        callback: (OSMDistanceResult) -> Unit
    ) {
        val url = "$OSRM_URL/$originLon,$originLat;$destLon,$destLat?overview=false&steps=false"
        
        val request = Request.Builder()
            .url(url)
            .build()
        
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Routing network error: ${e.message}")
                callback(OSMDistanceResult(
                    success = false,
                    errorMessage = "Nätverksfel vid ruttberäkning: ${e.message}"
                ))
            }
            
            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseBody = response.body?.string()
                    if (responseBody == null) {
                        callback(OSMDistanceResult(
                            success = false,
                            errorMessage = "Tom respons från ruttberäkning"
                        ))
                        return
                    }
                    
                    Log.d(TAG, "Routing response: $responseBody")
                    
                    val routeResponse = gson.fromJson(responseBody, OSRMResponse::class.java)
                    
                    if (routeResponse.code != "Ok" || routeResponse.routes.isEmpty()) {
                        val errorMsg = when (routeResponse.code) {
                            "NoRoute" -> "Ingen rutt hittades mellan adresserna"
                            "NoSegment" -> "Kunde inte hitta vägar nära start- eller slutpunkten"
                            else -> "Ruttberäkning misslyckades: ${routeResponse.code}"
                        }
                        callback(OSMDistanceResult(
                            success = false,
                            errorMessage = errorMsg
                        ))
                        return
                    }
                    
                    val route = routeResponse.routes[0]
                    val distanceKm = route.distance / 1000.0
                    val durationMinutes = (route.duration / 60).toInt()
                    
                    val distanceText = String.format("%.1f km", distanceKm)
                    val durationText = when {
                        durationMinutes < 60 -> "$durationMinutes min"
                        else -> {
                            val hours = durationMinutes / 60
                            val mins = durationMinutes % 60
                            "${hours}h ${mins}min"
                        }
                    }
                    
                    callback(OSMDistanceResult(
                        success = true,
                        distanceKm = distanceKm,
                        distanceText = distanceText,
                        durationText = durationText
                    ))
                    
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing routing response: ${e.message}")
                    callback(OSMDistanceResult(
                        success = false,
                        errorMessage = "Fel vid tolkning av ruttsvar: ${e.message}"
                    ))
                }
            }
        })
    }
}