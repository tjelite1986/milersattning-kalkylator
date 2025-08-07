package com.milersattning.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class GPSTrackingService(
    private val context: Context,
    private val onDistanceUpdate: (Double) -> Unit,
    private val onLocationUpdate: (Location) -> Unit
) {
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null
    private var isTracking = false
    private var totalDistance = 0.0
    private var lastLocation: Location? = null

    fun startTracking() {
        if (!hasLocationPermission()) {
            return
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000 // Update every 5 seconds
        ).apply {
            setMinUpdateDistanceMeters(10f) // Only update if moved 10 meters
            setMinUpdateIntervalMillis(2000) // Minimum 2 seconds between updates
            setMaxUpdateDelayMillis(10000) // Maximum 10 seconds delay
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    onLocationUpdate(location)
                    
                    lastLocation?.let { previousLocation ->
                        val distance = calculateDistance(previousLocation, location)
                        if (distance > 5) { // Only add significant movements (5+ meters)
                            totalDistance += distance
                            onDistanceUpdate(totalDistance)
                        }
                    }
                    
                    lastLocation = location
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )
            isTracking = true
            totalDistance = 0.0
            lastLocation = null
        } catch (e: SecurityException) {
            // Handle permission not granted
        }
    }

    fun stopTracking(): Double {
        locationCallback?.let { callback ->
            fusedLocationClient.removeLocationUpdates(callback)
        }
        isTracking = false
        locationCallback = null
        val finalDistance = totalDistance
        totalDistance = 0.0
        lastLocation = null
        return finalDistance / 1000.0 // Convert to kilometers
    }

    fun getCurrentDistance(): Double {
        return totalDistance / 1000.0 // Convert to kilometers
    }

    fun isCurrentlyTracking(): Boolean = isTracking

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun calculateDistance(loc1: Location, loc2: Location): Double {
        val earthRadiusMeters = 6371000.0
        
        val lat1Rad = Math.toRadians(loc1.latitude)
        val lat2Rad = Math.toRadians(loc2.latitude)
        val deltaLatRad = Math.toRadians(loc2.latitude - loc1.latitude)
        val deltaLonRad = Math.toRadians(loc2.longitude - loc1.longitude)

        val a = sin(deltaLatRad / 2) * sin(deltaLatRad / 2) +
                cos(lat1Rad) * cos(lat2Rad) *
                sin(deltaLonRad / 2) * sin(deltaLonRad / 2)
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        return earthRadiusMeters * c // Distance in meters
    }
}