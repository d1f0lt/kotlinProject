@file:Suppress("ktlint:standard:filename")

package com.example.weatherapp.data

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.json.JSONObject
import java.time.LocalDateTime

object ResponseStorage {
    private lateinit var weatherInfo: JSONObject
    private lateinit var curCity: String
    lateinit var lastUpdate: LocalDateTime private set

    private fun getApiKey(): String = "b6eda305f6904b0a9ab152658253005"

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateAndDoWithResponse(
        context: Context,
        callback: (JSONObject) -> Unit,
    ) {
        fetchData(curCity, context, callback)
    }

    fun doWithResponse(callback: (JSONObject) -> Unit) {
        assert(::curCity.isInitialized)
        callback(weatherInfo)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun doWithResponse(
        city: String,
        context: Context,
        callback: (JSONObject) -> Unit,
    ) {
        if (!::weatherInfo.isInitialized || (::curCity.isInitialized && curCity != city)) {
            fetchData(city, context, callback)
        } else {
            callback(weatherInfo)
        }
    }

    private fun buildUrl(city: String): String {
        val daysCount = 7
        return "https://api.weatherapi.com/v1/forecast.json?" +
            "key=${getApiKey()}&" +
            "q=$city&" +
            "days=$daysCount&" +
            "aqi=no&" +
            "alerts=no"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchData(
        city: String,
        context: Context,
        callback: (JSONObject) -> Unit,
    ) {
        val weatherApiUrl = buildUrl(city)

        val request =
            StringRequest(
                Request.Method.GET,
                weatherApiUrl,
                { response ->
                    weatherInfo = JSONObject(response)
                    curCity = city
                    lastUpdate = LocalDateTime.now()
                    callback(weatherInfo)
                },
                { error ->
                    Log.e("MyLog", "Volley error: ${error.message}")
                },
            )
        Volley.newRequestQueue(context).add(request)
    }
}
