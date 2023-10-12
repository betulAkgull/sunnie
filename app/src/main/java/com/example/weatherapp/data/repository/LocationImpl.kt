package com.example.weatherapp.data.repository

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.source.local.LocationService
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationImpl @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
) : LocationService {

    override suspend fun getCurrentLocation():
            Resource<Location> = suspendCancellableCoroutine { cont ->
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission
            || !isGpsEnabled
        ) {
            Resource.Error(Exception("Error"))
        }

        locationClient.lastLocation.apply {
            if (isComplete) {
                if (isSuccessful)
                    cont.resume(Resource.Success(Location(result.latitude, result.longitude)))
                else cont.resume(Resource.Error(Exception("Error")))
                return@suspendCancellableCoroutine
            }
            addOnSuccessListener {
                cont.resume(Resource.Success(Location(it.latitude, it.longitude)))
            }
            addOnFailureListener {
                cont.resume(
                    Resource.Error(Exception("Error"))
                )
            }
            addOnCanceledListener {
                cont.cancel()
            }
        }
    }

}
