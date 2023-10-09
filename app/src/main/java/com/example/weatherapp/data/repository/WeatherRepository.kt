package com.example.weatherapp.data.repository

import com.example.weatherapp.data.source.remote.WeatherService

class WeatherRepository(
    private val weatherService: WeatherService
) {
}