package com.example.weatherapp

import com.example.weatherapp.data.getHoursWeather
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class HoursWeatherParserTest {
    private lateinit var testResponse: JSONObject
    private val currentHour = LocalDateTime.now().hour

    @Before
    fun setup() {
        testResponse =
            JSONObject().apply {
                put(
                    "forecast",
                    JSONObject().apply {
                        put(
                            "forecastday",
                            JSONArray().apply {
                                put(
                                    JSONObject().apply {
                                        put(
                                            "hour",
                                            JSONArray().apply {
                                                for (h in 0 until 24) {
                                                    put(
                                                        JSONObject().apply {
                                                            put(
                                                                "time",
                                                                "2023-12-01 ${
                                                                    (
                                                                        if (h < 10) {
                                                                            "0"
                                                                        } else {
                                                                            ""
                                                                        }
                                                                    ) + h
                                                                }:00",
                                                            )
                                                            put("temp_c", 15.0 + h)
                                                            put(
                                                                "condition",
                                                                JSONObject().apply {
                                                                    put(
                                                                        "text",
                                                                        if (h < 12) "Sunny" else "Cloudy",
                                                                    )
                                                                    put(
                                                                        "icon",
                                                                        "//cdn.weatherapi.com/weather/64x64/day/${if (h < 12) 113 else 116}.png",
                                                                    )
                                                                },
                                                            )
                                                        },
                                                    )
                                                }
                                            },
                                        )
                                    },
                                )
                                // Завтра (day 1)
                                put(
                                    JSONObject().apply {
                                        put(
                                            "hour",
                                            JSONArray().apply {
                                                for (h in 0 until 24) {
                                                    put(
                                                        JSONObject().apply {
                                                            put(
                                                                "time",
                                                                "2023-12-02 ${
                                                                    (
                                                                        if (h < 10) {
                                                                            "0"
                                                                        } else {
                                                                            ""
                                                                        }
                                                                    ) + h
                                                                }:00",
                                                            )
                                                            put("temp_c", 10.0 + h)
                                                            put(
                                                                "condition",
                                                                JSONObject().apply {
                                                                    put(
                                                                        "text",
                                                                        if (h < 6) "Clear" else "Rainy",
                                                                    )
                                                                    put(
                                                                        "icon",
                                                                        "//cdn.weatherapi.com/weather/64x64/day/${if (h < 6) 113 else 176}.png",
                                                                    )
                                                                },
                                                            )
                                                        },
                                                    )
                                                }
                                            },
                                        )
                                    },
                                )
                            },
                        )
                    },
                )
            }
    }

    @Test
    fun test1() {
        val result = getHoursWeather(testResponse, 0)

        Assert.assertTrue(result.size <= 24 - currentHour)

        if (result.isNotEmpty()) {
            Assert.assertEquals("$currentHour:00", result[0].date)
            Assert.assertEquals("${15 + currentHour}°C", result[0].temperature)
        }
    }

    @Test
    fun test2() {
        val result = getHoursWeather(testResponse, 1)

        Assert.assertEquals(24, result.size)
        Assert.assertEquals("00:00", result[0].date)
        Assert.assertEquals("10°C", result[0].temperature)
        Assert.assertEquals("Clear", result[0].condition.condition)

        Assert.assertEquals("23:00", result[23].date)
        Assert.assertEquals("33°C", result[23].temperature)
        Assert.assertEquals("Rainy", result[23].condition.condition)
    }

    @Test(expected = JSONException::class)
    fun test3() {
        getHoursWeather(JSONObject("{}"))
    }
}
