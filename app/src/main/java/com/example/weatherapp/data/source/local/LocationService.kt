package com.example.weatherapp.data.source.local

import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.Location


interface LocationService {

    suspend fun getCurrentLocation(): Resource<Location>

}