package com.example.weatherapp.data.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.common.Constants
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.Day
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.source.remote.WeatherService

class WeatherRepository(
    private val weatherService: WeatherService

) {

    suspend fun getWeatherData(location:Location): Resource<List<Day>> {


        return try {


            val result = weatherService.getWeatherData(
                location.latitude,
                location.longitude,
                Constants.unitGroup,
                Constants.elements,
                Constants.days,
                BuildConfig.API_KEY,
                Constants.contentType
            ).days



            if (result.isNullOrEmpty()) {
                Resource.Error(Exception("error"))
            } else {
                Resource.Success(result)
            }


        } catch (e: Exception) {
            Resource.Error(e)
        }
    }


}