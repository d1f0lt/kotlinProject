package com.example.weatherapp.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationHelper(
    private val context: Context,
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getCurrentLocation(): Pair<Double, Double>? =
        suspendCoroutine { continuation ->
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
            ) { // на вызов функции ругается
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val lat = location.latitude
                            val lon = location.longitude
                            continuation.resume(lat to lon)
                        } else {
                            continuation.resume(null)
                        }
                    }.addOnFailureListener { e ->
                        Log.e("Location", "Ошибка: ${e.message}")
                        continuation.resume(null)
                    }
            } else {
                continuation.resume(null)
                return@suspendCoroutine
            }
        }
}
