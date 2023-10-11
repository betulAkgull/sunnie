package com.example.weatherapp.data.source.local

import com.example.weatherapp.data.model.Day
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {

    fun getLocation(): Response<Day>

}