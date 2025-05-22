package com.example.weatherapp.data

data class Condition(
    val imageUrl: String,
    val condition: String,
)

data class WeatherMainInfo(
    val date: String,
    val city: String,
    val lastUpdate: String,
    val curTemp: String,
    val maxTemp: String,
    val minTemp: String,
    val condition: Condition,
)

open class WeatherCardInfo(
    val date: String,
    val condition: Condition,
    val temperature: String,
)

class WeatherCardInfoPerDay(
    day: String,
    condition: Condition,
    minTemp: String,
    maxTemp: String,
) : WeatherCardInfo(day, condition, "$maxTemp/$minTemp")

class WeatherCardInfoPerHour(
    hour: String,
    condition: Condition,
    temp: String,
) : WeatherCardInfo(hour, condition, temp)
