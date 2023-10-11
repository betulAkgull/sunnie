package com.example.weatherapp.data.repository

import android.util.Log
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.Day
import com.example.weatherapp.data.source.remote.WeatherService

class WeatherRepository(
    private val weatherService: WeatherService
    //private val locationService: LocationService

) {


    suspend fun getWeatherData(): Resource<List<Day>> {
        val latitude = 40.7128 // Replace with your actual latitude value
        val longitude = -74.0060 // Replace with your actual longitude value

        return try {

            val result = weatherService.getWeatherData(
                latitude,
                longitude,
                "metric",
                "datetime,name,tempmax,tempmin,temp,humidity,precipprob,windspeed,uvindex,sunrise,sunset,icon",
                "days",
                "WB6X663R7Y89VDG6J2Q656DLQ",
                "json"
            ).days

            Log.e("repo", result.toString())

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