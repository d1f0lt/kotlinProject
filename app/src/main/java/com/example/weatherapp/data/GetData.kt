package com.example.weatherapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.time.LocalDate
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

@RequiresApi(Build.VERSION_CODES.O)
fun getDaysWeather(response: JSONObject): List<WeatherCardInfo> {
    val days = response.getJSONObject("forecast").getJSONArray("forecastday")
    val result = mutableListOf<WeatherCardInfo>()
    for (i in 0 until days.length()) {
        val item = days[i] as JSONObject

        val dayInfo = item.getJSONObject("day")
        result.add(
            WeatherCardInfoPerDay(
                day = (
                    if (i == 0) {
                        "Today"
                    } else if (i == 1) {
                        "Tomorrow"
                    } else {
                        LocalDate
                            .parse(
                                item.get("date") as String,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                            ).format(DateTimeFormatter.ofPattern("EEE"))
                    }
                ),
                condition =
                    Condition(
                        imageUrl = dayInfo.getJSONObject("condition").get("icon") as String,
                        condition = dayInfo.getJSONObject("condition").get("text") as String,
                    ),
                minTemp = proccessTemp(dayInfo.get("mintemp_c") as Double),
                maxTemp = proccessTemp(dayInfo.get("maxtemp_c") as Double),
            ),
        )
    }

    return result
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getHoursWeather(
    response: JSONObject,
    day: Int = 0,
): List<WeatherCardInfo> {
    val days = response.getJSONObject("forecast").getJSONArray("forecastday")
    val result = mutableListOf<WeatherCardInfo>()

    val dayInfo = (days[day] as JSONObject).getJSONArray("hour")
    for (i in 0 until dayInfo.length()) {
        val hour = dayInfo[i] as JSONObject
        result.add(
            WeatherCardInfoPerHour(
                hour =
                    formatDate(
                        hour.get("time") as String,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                        DateTimeFormatter.ofPattern("HH:mm"),
                    ),
                condition =
                    Condition(
                        imageUrl = hour.getJSONObject("condition").get("icon") as String,
                        condition = hour.getJSONObject("condition").get("text") as String,
                    ),
                temp = proccessTemp(hour.get("temp_c") as Double),
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
        ResponseStorage.lastUpdate.format(
            DateTimeFormatter.ofPattern(
                "d MMM HH:mm",
                Locale.ENGLISH,
            ),
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
        val dayInfo = day.getJSONObject("day")
        curTemp = proccessTemp(dayInfo.get("avgtemp_c") as Double)
        condition =
            Condition(
                imageUrl = dayInfo.getJSONObject("condition").get("icon") as String,
                condition = dayInfo.getJSONObject("condition").get("text") as String,
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
        hours = getHoursWeather(response, dayShift),
    )
}
