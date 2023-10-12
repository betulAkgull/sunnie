package com.example.weatherapp.data.repository

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.source.local.LocationService
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationImpl @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
) : LocationService {

    override suspend fun getCurrentLocation(): Resource<Location> {

        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission) {
            return Resource.Error(Exception("Location permissions are not granted"))
        }

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled) {
            return Resource.Error(Exception("GPS is not enabled"))
        }

        return try {
            val location = locationClient.lastLocation.await()
            if (location != null) {
                Log.e("lat",location.latitude.toString())
                Resource.Success(Location(location.latitude, location.longitude))
            } else {
                Resource.Error(Exception("Location not available"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

}
