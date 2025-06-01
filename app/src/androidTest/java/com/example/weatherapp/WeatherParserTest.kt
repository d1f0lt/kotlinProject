package com.example.weatherapp

import com.example.weatherapp.data.getDaysWeather
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeatherParserTest {
    private lateinit var testResponse: JSONObject

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
                                        put("date", LocalDate.now().toString())
                                        put(
                                            "day",
                                            JSONObject().apply {
                                                put("mintemp_c", 10.5)
                                                put("maxtemp_c", 20.0)
                                                put(
                                                    "condition",
                                                    JSONObject().apply {
                                                        put("text", "Sunny")
                                                        put("icon", "//cdn.weatherapi.com/weather/64x64/day/113.png")
                                                    },
                                                )
                                            },
                                        )
                                    },
                                )
                                // Завтра
                                put(
                                    JSONObject().apply {
                                        put("date", LocalDate.now().plusDays(1).toString())
                                        put(
                                            "day",
                                            JSONObject().apply {
                                                put("mintemp_c", 12.3)
                                                put("maxtemp_c", 22.7)
                                                put(
                                                    "condition",
                                                    JSONObject().apply {
                                                        put("text", "Cloudy")
                                                        put("icon", "//cdn.weatherapi.com/weather/64x64/day/116.png")
                                                    },
                                                )
                                            },
                                        )
                                    },
                                )
                                put(
                                    JSONObject().apply {
                                        put("date", LocalDate.now().plusDays(2).toString())
                                        put(
                                            "day",
                                            JSONObject().apply {
                                                put("mintemp_c", 8.1)
                                                put("maxtemp_c", 18.9)
                                                put(
                                                    "condition",
                                                    JSONObject().apply {
                                                        put("text", "Rainy")
                                                        put("icon", "//cdn.weatherapi.com/weather/64x64/day/176.png")
                                                    },
                                                )
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
        val result = getDaysWeather(testResponse)

        Assert.assertEquals(3, result.size)
        Assert.assertEquals("Today", result[0].date)
        Assert.assertEquals("Sunny", result[0].condition.condition)
        Assert.assertEquals("20°C/10°C", result[0].temperature)
    }

    @Test
    fun test2() {
        val result = getDaysWeather(testResponse)

        Assert.assertEquals("Tomorrow", result[1].date)
        Assert.assertEquals("Cloudy", result[1].condition.condition)
        Assert.assertEquals("23°C/12°C", result[1].temperature)
    }

    @Test
    fun test3() {
        val result = getDaysWeather(testResponse)
        val expectedDay =
            LocalDate
                .now()
                .plusDays(2)
                .format(DateTimeFormatter.ofPattern("EEE"))

        Assert.assertEquals(expectedDay, result[2].date)
        Assert.assertEquals("Rainy", result[2].condition.condition)
        Assert.assertEquals("19°C/8°C", result[2].temperature)
    }

    @Test
    fun test4() {
        val emptyResponse =
            JSONObject().apply {
                put(
                    "forecast",
                    JSONObject().apply {
                        put("forecastday", JSONArray())
                    },
                )
            }

        val result = getDaysWeather(emptyResponse)
        Assert.assertTrue(result.isEmpty())
    }

    @Test(expected = JSONException::class)
    fun test5() {
        getDaysWeather(JSONObject("{}"))
    }

    @Test
    fun test6() {
        val tempTestResponse =
            JSONObject().apply {
                put(
                    "forecast",
                    JSONObject().apply {
                        put(
                            "forecastday",
                            JSONArray().apply {
                                put(
                                    JSONObject().apply {
                                        put("date", LocalDate.now().toString())
                                        put(
                                            "day",
                                            JSONObject().apply {
                                                put("mintemp_c", 10.499)
                                                put("maxtemp_c", 20.501)
                                                put(
                                                    "condition",
                                                    JSONObject().apply {
                                                        put("text", "")
                                                        put("icon", "")
                                                    },
                                                )
                                            },
                                        )
                                    },
                                )
                            },
                        )
                    },
                )
            }

        val result = getDaysWeather(tempTestResponse)
        Assert.assertEquals("21°C/10°C", result[0].temperature)
    }
}
