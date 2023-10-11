package com.example.weatherapp.data.model.response

import com.example.weatherapp.data.model.Day

data class GetWeatherResponse(
    val days: List<Day>?
)