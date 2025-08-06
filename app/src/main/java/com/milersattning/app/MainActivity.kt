package com.milersattning.app

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

data class Trip(val distance: Double, val amount: Double, val date: String, val description: String = "")

class MainActivity : AppCompatActivity() {
    
    private lateinit var prefs: SharedPreferences
    private lateinit var osmService: OpenStreetMapService
    private var currentRate = 1.85
    private var trips = mutableListOf<Trip>()
    private var favoritePlaces = mutableListOf<FavoritePlace>()
    private var isUsingAddresses = false
    private var lastCalculatedDistance = 0.0
    
    // UI Elements
    private lateinit var addressModeToggle: RadioGroup
    private lateinit var addressModeRadio: RadioButton
    private lateinit var manualModeRadio: RadioButton
    private lateinit var addressLayout: LinearLayout
    private lateinit var fromAddressInput: AutoCompleteTextView
    private lateinit var toAddressInput: AutoCompleteTextView
    private lateinit var distanceInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var calculateButton: Button
    private lateinit var saveButton: Button
    private lateinit var settingsButton: Button
    private lateinit var viewTripsButton: Button
    private lateinit var resultText: TextView
    private lateinit var rateText: TextView
    private lateinit var totalText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var fromFavoritesButton: Button
    private lateinit var toFavoritesButton: Button
    private lateinit var saveFromFavoriteButton: Button
    private lateinit var manageFavoritesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_with_addresses)

        prefs = getSharedPreferences("milersattning_prefs", Context.MODE_PRIVATE)
        currentRate = prefs.getFloat("rate", 1.85f).toDouble()
        osmService = OpenStreetMapService()
        
        initViews()
        loadTrips()
        loadFavoritePlaces()
        updateUI()
        setupClickListeners()
    }
    
    private fun initViews() {
        addressModeToggle = findViewById(R.id.mode_toggle)
        addressModeRadio = findViewById(R.id.address_mode_radio)
        manualModeRadio = findViewById(R.id.manual_mode_radio)
        addressLayout = findViewById(R.id.address_layout)
        fromAddressInput = findViewById(R.id.from_address_input)
        toAddressInput = findViewById(R.id.to_address_input)
        distanceInput = findViewById(R.id.distance_input)
        descriptionInput = findViewById(R.id.description_input)
        calculateButton = findViewById(R.id.calculate_button)
        saveButton = findViewById(R.id.save_button)
        settingsButton = findViewById(R.id.settings_button)
        viewTripsButton = findViewById(R.id.view_trips_button)
        resultText = findViewById(R.id.result_text)
        rateText = findViewById(R.id.rate_text)
        totalText = findViewById(R.id.total_text)
        progressBar = findViewById(R.id.progress_bar)
        fromFavoritesButton = findViewById(R.id.from_favorites_button)
        toFavoritesButton = findViewById(R.id.to_favorites_button)
        saveFromFavoriteButton = findViewById(R.id.save_from_favorite_button)
        manageFavoritesButton = findViewById(R.id.manage_favorites_button)
        
        // Konfigurera autocomplete för adressfält
        setupAddressAutocomplete()
        setupFavoriteButtons()
    }
    
    private fun setupAddressAutocomplete() {
        val fromAdapter = AddressAutocompleteAdapter(this)
        val toAdapter = AddressAutocompleteAdapter(this)
        
        fromAddressInput.setAdapter(fromAdapter)
        toAddressInput.setAdapter(toAdapter)
        
        // Ställ in att visa förslag efter 2 tecken
        fromAddressInput.threshold = 2
        toAddressInput.threshold = 2
        
        // Hantera selection av förslag
        fromAddressInput.setOnItemClickListener { _, _, position, _ ->
            val selectedAddress = fromAdapter.getSelectedAddress(position)
            fromAddressInput.setText(selectedAddress)
            fromAddressInput.setSelection(selectedAddress.length)
        }
        
        toAddressInput.setOnItemClickListener { _, _, position, _ ->
            val selectedAddress = toAdapter.getSelectedAddress(position)
            toAddressInput.setText(selectedAddress)
            toAddressInput.setSelection(selectedAddress.length)
        }
    }
    
    private fun setupFavoriteButtons() {
        fromAddressInput.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                saveFromFavoriteButton.isEnabled = !s.isNullOrBlank()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        
        fromFavoritesButton.setOnClickListener {
            showFavoriteSelector { favorite ->
                fromAddressInput.setText(favorite.address)
                fromAddressInput.setSelection(favorite.address.length)
            }
        }
        
        toFavoritesButton.setOnClickListener {
            showFavoriteSelector { favorite ->
                toAddressInput.setText(favorite.address)
                toAddressInput.setSelection(favorite.address.length)
            }
        }
        
        saveFromFavoriteButton.setOnClickListener {
            showAddFavoriteDialog(fromAddressInput.text.toString())
        }
        
        manageFavoritesButton.setOnClickListener {
            showManageFavoritesDialog()
        }
    }
    
    private fun updateUI() {
        rateText.text = String.format("Milersättning: %.2f SEK/km", currentRate)
        val totalAmount = trips.sumOf { it.amount }
        totalText.text = String.format("Totalt sparat: %.2f SEK (%d resor)", totalAmount, trips.size)
    }
    
    private fun setupClickListeners() {
        addressModeToggle.setOnCheckedChangeListener { _, checkedId ->
            isUsingAddresses = checkedId == R.id.address_mode_radio
            updateModeUI()
        }
        
        calculateButton.setOnClickListener {
            if (isUsingAddresses) {
                calculateDistanceFromAddresses()
            } else {
                calculateAmount()
            }
        }
        
        saveButton.setOnClickListener {
            saveTrip()
        }
        
        settingsButton.setOnClickListener {
            showSettingsDialog()
        }
        
        viewTripsButton.setOnClickListener {
            showTripsDialog()
        }
    }
    
    private fun updateModeUI() {
        if (isUsingAddresses) {
            addressLayout.visibility = View.VISIBLE
            findViewById<LinearLayout>(R.id.manual_distance_layout).visibility = View.GONE
        } else {
            addressLayout.visibility = View.GONE
            findViewById<LinearLayout>(R.id.manual_distance_layout).visibility = View.VISIBLE
        }
        
        // Reset result when switching modes
        resultText.text = "Belopp: 0.00 SEK"
        saveButton.isEnabled = false
        lastCalculatedDistance = 0.0
    }
    
    private fun calculateDistanceFromAddresses() {
        val fromAddress = fromAddressInput.text.toString().trim()
        val toAddress = toAddressInput.text.toString().trim()
        
        if (fromAddress.isEmpty() || toAddress.isEmpty()) {
            resultText.text = "Ange både från- och tilladress"
            saveButton.isEnabled = false
            return
        }
        
        // Show loading state
        progressBar.visibility = View.VISIBLE
        calculateButton.isEnabled = false
        resultText.text = getString(R.string.calculating_distance)
        
        osmService.calculateDistance(fromAddress, toAddress) { result ->
            Handler(Looper.getMainLooper()).post {
                // Hide loading state
                progressBar.visibility = View.GONE
                calculateButton.isEnabled = true
                
                if (result.success) {
                    lastCalculatedDistance = result.distanceKm
                    val total = result.distanceKm * currentRate
                    resultText.text = String.format(
                        "Distans: %.1f km\nBelopp: %.2f SEK",
                        result.distanceKm, total
                    )
                    if (result.durationText.isNotEmpty()) {
                        resultText.text = "${resultText.text}\nRestid: ${result.durationText}"
                    }
                    saveButton.isEnabled = true
                } else {
                    resultText.text = "Fel: ${result.errorMessage}"
                    saveButton.isEnabled = false
                    lastCalculatedDistance = 0.0
                }
            }
        }
    }
    
    private fun calculateAmount() {
        val distanceStr = distanceInput.text.toString()
        if (distanceStr.isNotEmpty()) {
            try {
                val distance = distanceStr.toDouble()
                val total = distance * currentRate
                resultText.text = String.format("Belopp: %.2f SEK", total)
                saveButton.isEnabled = true
            } catch (e: NumberFormatException) {
                resultText.text = "Ogiltigt värde"
                saveButton.isEnabled = false
            }
        } else {
            resultText.text = "Ange distans"
            saveButton.isEnabled = false
        }
    }
    
    private fun saveTrip() {
        val description = if (isUsingAddresses) {
            val from = fromAddressInput.text.toString().trim()
            val to = toAddressInput.text.toString().trim()
            val customDesc = descriptionInput.text.toString().trim()
            if (customDesc.isNotEmpty()) {
                "$customDesc ($from → $to)"
            } else {
                "$from → $to"
            }
        } else {
            descriptionInput.text.toString().ifEmpty { "Resa" }
        }
        
        val distance = if (isUsingAddresses) {
            lastCalculatedDistance
        } else {
            val distanceStr = distanceInput.text.toString()
            if (distanceStr.isNotEmpty()) {
                try {
                    distanceStr.toDouble()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Ogiltigt värde", Toast.LENGTH_SHORT).show()
                    return
                }
            } else {
                0.0
            }
        }
        
        if (distance > 0) {
            val amount = distance * currentRate
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            
            val trip = Trip(distance, amount, date, description)
            trips.add(trip)
            saveTrips()
            
            // Clear inputs
            if (isUsingAddresses) {
                fromAddressInput.text.clear()
                toAddressInput.text.clear()
            } else {
                distanceInput.text.clear()
            }
            descriptionInput.text.clear()
            resultText.text = "Resa sparad!"
            saveButton.isEnabled = false
            lastCalculatedDistance = 0.0
            
            updateUI()
            Toast.makeText(this, "Resa sparad: ${String.format("%.1f km, %.2f SEK", distance, amount)}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Ingen giltig distans att spara", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showSettingsDialog() {
        val input = EditText(this)
        input.setText(currentRate.toString())
        input.hint = "SEK per kilometer"
        
        AlertDialog.Builder(this)
            .setTitle("Ändra milersättning")
            .setMessage("Ange ny ersättning per kilometer:")
            .setView(input)
            .setPositiveButton("Spara") { _, _ ->
                try {
                    val newRate = input.text.toString().toDouble()
                    if (newRate > 0) {
                        currentRate = newRate
                        prefs.edit().putFloat("rate", newRate.toFloat()).apply()
                        updateUI()
                        Toast.makeText(this, "Ny ersättning: ${String.format("%.2f SEK/km", newRate)}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Ersättning måste vara större än 0", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Ogiltigt värde", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Avbryt", null)
            .show()
    }
    
    private fun showTripsDialog() {
        if (trips.isEmpty()) {
            Toast.makeText(this, "Inga sparade resor", Toast.LENGTH_SHORT).show()
            return
        }
        
        val tripStrings = trips.map { trip ->
            "${trip.description}: ${String.format("%.1f km - %.2f SEK", trip.distance, trip.amount)} (${trip.date})"
        }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Sparade resor (${trips.size})")
            .setItems(tripStrings, null)
            .setPositiveButton("Rensa alla") { _, _ ->
                AlertDialog.Builder(this)
                    .setTitle("Bekräfta")
                    .setMessage("Vill du verkligen ta bort alla sparade resor?")
                    .setPositiveButton("Ja") { _, _ ->
                        trips.clear()
                        saveTrips()
                        updateUI()
                        Toast.makeText(this, "Alla resor borttagna", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Nej", null)
                    .show()
            }
            .setNegativeButton("Stäng", null)
            .show()
    }
    
    private fun saveTrips() {
        val tripsJson = trips.joinToString(";") { trip ->
            "${trip.distance},${trip.amount},${trip.date},${trip.description}"
        }
        prefs.edit().putString("trips", tripsJson).apply()
    }
    
    private fun loadTrips() {
        val tripsJson = prefs.getString("trips", "") ?: ""
        if (tripsJson.isNotEmpty()) {
            trips.clear()
            tripsJson.split(";").forEach { tripStr ->
                try {
                    val parts = tripStr.split(",", limit = 4)
                    if (parts.size >= 3) {
                        val distance = parts[0].toDouble()
                        val amount = parts[1].toDouble()
                        val date = parts[2]
                        val description = if (parts.size > 3) parts[3] else "Resa"
                        trips.add(Trip(distance, amount, date, description))
                    }
                } catch (e: Exception) {
                    // Skip invalid entries
                }
            }
        }
    }
    
    private fun loadFavoritePlaces() {
        val favoritesJson = prefs.getString("favorite_places", "") ?: ""
        if (favoritesJson.isNotEmpty()) {
            favoritePlaces.clear()
            favoritesJson.split(";").forEach { favoriteStr ->
                try {
                    val parts = favoriteStr.split("|", limit = 5)
                    if (parts.size >= 4) {
                        val id = parts[0]
                        val name = parts[1]
                        val address = parts[2]
                        val displayName = if (parts.size > 4) parts[4] else parts[2]
                        val dateAdded = parts[3]
                        favoritePlaces.add(FavoritePlace(id, name, address, displayName, dateAdded))
                    }
                } catch (e: Exception) {
                    // Skip invalid entries
                }
            }
        }
    }
    
    private fun saveFavoritePlaces() {
        val favoritesJson = favoritePlaces.joinToString(";") { favorite ->
            "${favorite.id}|${favorite.name}|${favorite.address}|${favorite.dateAdded}|${favorite.displayName}"
        }
        prefs.edit().putString("favorite_places", favoritesJson).apply()
    }
    
    private fun addFavoritePlace(name: String, address: String, displayName: String) {
        val id = UUID.randomUUID().toString()
        val dateAdded = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val favorite = FavoritePlace(id, name, address, displayName, dateAdded)
        favoritePlaces.add(favorite)
        saveFavoritePlaces()
    }
    
    private fun removeFavoritePlace(favoritePlace: FavoritePlace) {
        favoritePlaces.remove(favoritePlace)
        saveFavoritePlaces()
    }
    
    private fun showFavoriteSelector(onSelected: (FavoritePlace) -> Unit) {
        if (favoritePlaces.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_favorites), Toast.LENGTH_SHORT).show()
            return
        }
        
        val favoriteNames = favoritePlaces.map { "${it.name} (${it.displayName})" }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.favorites))
            .setItems(favoriteNames) { _, which ->
                onSelected(favoritePlaces[which])
            }
            .setNegativeButton("Avbryt", null)
            .show()
    }
    
    private fun showAddFavoriteDialog(address: String) {
        if (address.isBlank()) {
            Toast.makeText(this, "Ange en adress först", Toast.LENGTH_SHORT).show()
            return
        }
        
        val input = EditText(this)
        input.hint = getString(R.string.favorite_name_hint)
        
        AlertDialog.Builder(this)
            .setTitle("Lägg till favorit")
            .setMessage("Ange namn för favoriten:")
            .setView(input)
            .setPositiveButton("Spara") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    addFavoritePlace(name, address, address)
                    Toast.makeText(this, "Favorit '$name' tillagd", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Ange ett namn för favoriten", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Avbryt", null)
            .show()
    }
    
    private fun showManageFavoritesDialog() {
        if (favoritePlaces.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_favorites), Toast.LENGTH_SHORT).show()
            return
        }
        
        val favoriteStrings = favoritePlaces.map { favorite ->
            "${favorite.name}: ${favorite.displayName} (${favorite.dateAdded})"
        }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Hantera favoriter (${favoritePlaces.size})")
            .setItems(favoriteStrings) { _, which ->
                val favorite = favoritePlaces[which]
                AlertDialog.Builder(this)
                    .setTitle("Favorit: ${favorite.name}")
                    .setMessage("Vad vill du göra med denna favorit?\n\nAdress: ${favorite.displayName}")
                    .setPositiveButton("Ta bort") { _, _ ->
                        AlertDialog.Builder(this)
                            .setTitle("Bekräfta")
                            .setMessage("Vill du verkligen ta bort favoriten '${favorite.name}'?")
                            .setPositiveButton("Ja") { _, _ ->
                                removeFavoritePlace(favorite)
                                Toast.makeText(this, "Favorit '${favorite.name}' borttagen", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("Nej", null)
                            .show()
                    }
                    .setNeutralButton("Använd som från-adress") { _, _ ->
                        fromAddressInput.setText(favorite.address)
                        fromAddressInput.setSelection(favorite.address.length)
                    }
                    .setNegativeButton("Stäng", null)
                    .show()
            }
            .setNegativeButton("Stäng", null)
            .show()
    }
}