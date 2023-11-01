package com.example.weatherapp.data.model


data class DayUI(
    val datetime: String,
    val humidity: Double,
    val icon: String,
    val precipprob: Double,
    val sunrise: String,
    val sunset: String,
    val temp: Double,
    val tempmax: Double,
    val tempmin: Double,
    val uvindex: Double,
    val windspeed: Double
)