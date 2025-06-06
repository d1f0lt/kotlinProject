package com.example.weatherapp.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.weatherapp.components.Background
import com.example.weatherapp.components.DialogSearch
import com.example.weatherapp.components.MainCard
import com.example.weatherapp.components.Tabs
import com.example.weatherapp.data.ResponseStorage
import com.example.weatherapp.data.WeatherMainInfo
import com.example.weatherapp.data.getCity
import com.example.weatherapp.data.getDaysWeather
import com.example.weatherapp.data.getMainScreenInfo
import com.example.weatherapp.helpers.LocationHelper
import org.json.JSONObject

@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Suppress("ktlint:standard:function-naming")
@Composable
fun MainScreen(context: Context) {
    var weatherData by remember { mutableStateOf<JSONObject?>(null) }
    var city by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        city = getCity(context, LocationHelper(context))
    }

    LaunchedEffect(city) {
        city?.let {
            ResponseStorage.doWithResponse(city!!, context) { json ->
                weatherData = json
            }
        }
    }

    if (weatherData == null) {
        LoadingScreen()
    } else {
        Background()
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            var mainCardData =
                mutableStateOf<WeatherMainInfo>(
                    getMainScreenInfo(
                        weatherData!!,
                    ),
                )

            val searchDialogFlag = remember { mutableStateOf(false) }

            if (searchDialogFlag.value) {
                DialogSearch(searchDialogFlag, onSubmit = { newCity ->
                    city = newCity
                })
            }

            MainCard(mainCardData, context, onSearchClick = { searchDialogFlag.value = true })
            Tabs(mainCardData, getDaysWeather(weatherData!!))
        }
    }
}
