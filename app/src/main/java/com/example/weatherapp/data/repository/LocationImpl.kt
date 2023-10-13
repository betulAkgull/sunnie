package com.example.weatherapp.data.repository

import android.app.Application
import android.location.Geocoder
import android.util.Log
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.utils.LocationUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.type.LatLng
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

class LocationImpl @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
) : LocationService {

    override suspend fun getCurrentLocation(): Resource<Location> {

        if (!LocationUtil.isGpsEnabled(application)) {
            return Resource.Error(Exception("GPS is not enabled"))
        }

        val location: LatLng

        if (LocationUtil.checkLocationPermission(application)) {
            location = locationClient.lastLocation.await().let {
                LatLng.newBuilder()
                    .setLatitude(it.latitude)
                    .setLongitude(it.longitude)
                    .build()
            }
        } else {
            return Resource.Error(Exception("Location permission is not granted"))
        }

        val city = getCityName(location.latitude, location.longitude)
        return Resource.Success(Location(location.latitude, location.longitude, city))
    }

    private fun getCityName(lat: Double, lng: Double): String {
        val geocoder = Geocoder(application, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lng, 1)
        return addresses?.getOrNull(0)?.adminArea ?: "Unknown"
    }
}

interface LocationService {
    suspend fun getCurrentLocation(): Resource<Location>
}
