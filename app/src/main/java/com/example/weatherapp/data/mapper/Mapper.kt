package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.model.SavedLocationsEntity

fun Location.mapToSavedLocationEntity(): SavedLocationsEntity {
    return SavedLocationsEntity(
        latitude = latitude,
        longitude = longitude,
        city = city,
        province = province,
    )
}