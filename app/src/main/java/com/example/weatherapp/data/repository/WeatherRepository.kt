package com.example.weatherapp.data.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.common.Constants
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.mapper.mapToDayUI
import com.example.weatherapp.data.mapper.mapToLocation
import com.example.weatherapp.data.mapper.mapToSavedLocationEntity
import com.example.weatherapp.data.model.DayUI
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.source.local.SavedLocationsDao
import com.example.weatherapp.data.source.remote.WeatherService

class WeatherRepository(
    private val weatherService: WeatherService,
    private val savedLocationsDao: SavedLocationsDao

) {

    suspend fun getWeatherData(location: Location): Resource<List<DayUI>> {
        return try {
            val result = weatherService.getWeatherData(
                location.latitude,
                location.longitude,
                Constants.unitGroup,
                Constants.elements,
                Constants.days,
                BuildConfig.API_KEY,
                Constants.contentType
            ).days.orEmpty()

            if (result.isEmpty()) {
                Resource.Error(Exception("Weather data not found"))
            } else {
                Resource.Success(result.map {
                    it.mapToDayUI()
                })
            }

        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun addToSavedLocations(location: Location) {
        savedLocationsDao.addToSavedLocations(location.mapToSavedLocationEntity())
    }

    suspend fun removeFromSavedLocations(location: Location) {
        savedLocationsDao.removeFromSavedLocations(location.mapToSavedLocationEntity())
    }

    suspend fun getSavedLocations(): Resource<List<Location>> {
        return try {
            val result = savedLocationsDao.getSavedLocations().map {
                it.mapToLocation()
            }
            if (result.isEmpty()) {
                Resource.Error(Exception("There are no saved locations"))
            } else {
                Resource.Success(result)
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }


}