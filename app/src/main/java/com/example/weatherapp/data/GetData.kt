package com.example.weatherapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.round

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(
    dateStr: String,
    fromPattern: DateTimeFormatter,
    toPattern: DateTimeFormatter,
): String = LocalDateTime.parse(dateStr, fromPattern).format(toPattern)

fun getDaysWeather(response: JSONObject): List<WeatherCardInfo> {
    val days = response.getJSONObject("forecast").getJSONArray("forecastday")
    val result = mutableListOf<WeatherCardInfo>()
    for (i in 0 until days.length()) {
        val item = days[i] as JSONObject

        val dayInfo = item.getJSONObject("day")
        result.add(
            WeatherCardInfoPerDay(
                day = item.get("date") as String,
                condition =
                    Condition(
                        imageUrl = dayInfo.getJSONObject("condition").get("icon") as String,
                        condition = dayInfo.getJSONObject("condition").get("text") as String,
                    ),
                minTemp = dayInfo.get("mintemp_c") as String,
                maxTemp = dayInfo.get("maxtemp_c") as String,
            ),
        )
    }

    return result
}

fun getHourWeather(
    response: JSONObject,
    day: Int,
): List<WeatherCardInfo> {
    val days = response.getJSONObject("forecast").getJSONArray("forecastday")
    val result = mutableListOf<WeatherCardInfo>()

    val dayInfo = (days[day] as JSONObject).getJSONArray("hour")
    for (i in 0 until dayInfo.length()) {
        val hour = dayInfo[i] as JSONObject
        result.add(
            WeatherCardInfoPerHour(
                hour = hour.get("time") as String,
                condition =
                    Condition(
                        imageUrl = hour.getJSONObject("condition").get("icon") as String,
                        condition = hour.getJSONObject("condition").get("text") as String,
                    ),
                temp = hour.get("temp_c") as String,
            ),
        )
    }

    return result
}

private fun proccessTemp(temp: Double) = round(temp).toInt().toString() + "°C"

@RequiresApi(Build.VERSION_CODES.O)
fun getMainScreenInfo(
    response: JSONObject,
    dayShift: Int = 0,
): WeatherMainInfo {
    val city = response.getJSONObject("location").get("name") as String
    val lastUpdate =
        formatDate(
            response.getJSONObject("current").get("last_updated") as String,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("d MMM HH:mm", Locale.ENGLISH),
        )
    val day = response.getJSONObject("forecast").getJSONArray("forecastday")[dayShift] as JSONObject
    val maxTemp = proccessTemp(day.getJSONObject("day").get("maxtemp_c") as Double)
    val minTemp = proccessTemp(day.getJSONObject("day").get("mintemp_c") as Double)
    var date: String
    val curTemp: String
    val condition: Condition
    if (dayShift == 0) {
        date =
            formatDate(
                response.getJSONObject("location").get("localtime") as String,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            )
        val current = response.getJSONObject("current")
        curTemp = proccessTemp(current.get("temp_c") as Double)
        condition =
            Condition(
                imageUrl = current.getJSONObject("condition").get("icon") as String,
                condition = current.getJSONObject("condition").get("text") as String,
            )
    } else {
        date = day.get("date") as String
        curTemp = proccessTemp(day.getJSONObject("day").get("avgtemp_c") as Double)
        condition =
            Condition(
                imageUrl = day.getJSONObject("condition").get("icon") as String,
                condition = day.getJSONObject("condition").get("text") as String,
            )
    }

    return WeatherMainInfo(
        date = date,
        city = city,
        lastUpdate = lastUpdate,
        curTemp = curTemp,
        maxTemp = maxTemp,
        minTemp = minTemp,
        condition = condition,
    )
}
