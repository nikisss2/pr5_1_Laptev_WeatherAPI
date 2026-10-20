package com.example.pr5_weather_laptev

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class WeatherApiTest {

    private val gson = Gson()

    @Test
    fun testSerialization() {
        val weather = WeatherData("Екатеринбург", 1.8, 1029, 64, 9.0, "пасмурно")
        val json = gson.toJson(weather)
        assertNotNull(json)
    }

    @Test
    fun testDeserialization() {
        val json = """{"cityName":"Екатеринбург","temp":1.8,"pressure":1029,"humidity":64,"windSpeed":9.0,"description":"пасмурно"}"""
        val weather = gson.fromJson(json, WeatherData::class.java)
        assertEquals("Екатеринбург", weather.cityName)
        assertEquals(1.8, weather.temp, 0.01)
        assertEquals(1029, weather.pressure)
        assertEquals(64, weather.humidity)
        assertEquals(9.0, weather.windSpeed, 0.01)
        assertEquals("пасмурно", weather.description)
    }
}
