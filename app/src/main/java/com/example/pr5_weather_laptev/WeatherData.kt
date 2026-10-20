package com.example.pr5_weather_laptev

data class WeatherData(
    val cityName: String,
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double,
    val description: String
)
