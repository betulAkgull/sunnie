package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.model.Day
import com.example.weatherapp.data.model.DayUI
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

fun SavedLocationsEntity.mapToLocation(): Location {
    return Location(
        latitude = latitude,
        longitude = longitude,
        city = city,
        province = province,
    )
}

fun Day.mapToDayUI(): DayUI {
    return DayUI(
        datetime = datetime.orEmpty(),
        humidity = humidity ?: 0.0,
        icon = icon.orEmpty(),
        precipprob = precipprob ?: 0.0,
        sunrise = sunrise.orEmpty(),
        sunset = sunset.orEmpty(),
        temp = temp ?: 0.0,
        tempmax = tempmax ?: 0.0,
        tempmin = tempmin ?: 0.0,
        uvindex = uvindex ?: 0.0,
        windspeed = windspeed ?: 0.0
    )
}