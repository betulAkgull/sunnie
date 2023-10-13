package com.example.weatherapp.data.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object LocationUtil {

    const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    const val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

    fun checkLocationPermission(context: Context): Boolean {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return !(!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission)
    }

    fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
    }
}