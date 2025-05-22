@file:Suppress("ktlint:standard:filename")

package com.example.weatherapp.data

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.json.JSONObject

object ResponseStorage {
    private lateinit var weatherInfo: JSONObject
    private lateinit var curCity: String

    private fun getApiKey(): String = "f2cd0ec5671e4c85b6f83223251205"

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
                    callback(weatherInfo)
                },
                { error ->
                    Log.e("MyLog", "Volley error: ${error.message}")
                },
            )
        Volley.newRequestQueue(context).add(request)
    }
}
