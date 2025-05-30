package com.example.weatherapp.data

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.helpers.LocationHelper
import org.json.JSONObject
import java.util.Locale
import kotlin.collections.isNotEmpty
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
suspend fun getCity(
    context: Context,
    locationHelper: LocationHelper,
): String =
    locationHelper.getCurrentLocation()?.let { cord ->
        getCityByLocation(context, cord.first, cord.second)
    } ?: getCityByIp(context)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private suspend fun getCityByLocation(
    context: Context,
    lat: Double,
    lon: Double,
): String =
    suspendCoroutine { continuation ->
        val geocoder = Geocoder(context, Locale.getDefault())

        geocoder.getFromLocation(
            lat,
            lon,
            1,
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    if (addresses.isNotEmpty()) {
                        val city = addresses[0].locality ?: "London"
                        continuation.resume(city)
                    } else {
                        continuation.resume("London")
                    }
                }

                override fun onError(errorMessage: String?) {
                    Log.e("MyLog", "Geocoder error: $errorMessage")
                    continuation.resume("London")
                }
            },
        )
    }

private suspend fun getCityByIp(context: Context): String =
    suspendCoroutine { continuation ->
        val url = "http://ip-api.com/json/?fields=city"

        val request =
            StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    try {
                        continuation.resume(JSONObject(response).getString("city"))
                    } catch (e: Exception) {
                        continuation.resume("London") // default city
                    }
                },
                { error ->
                    Log.e("MyLog", "Volley error: ${error.message}")
                    continuation.resume("London")
                },
            )
        Volley.newRequestQueue(context).add(request)
    }
