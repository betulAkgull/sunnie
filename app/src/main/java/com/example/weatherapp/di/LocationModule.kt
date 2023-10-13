package com.example.weatherapp.di


import android.app.Application
import com.example.weatherapp.data.repository.LocationImpl
import com.example.weatherapp.data.repository.LocationService
import com.google.android.gms.location.LocationServices

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    fun provideLocationService(locationImpl: LocationImpl): LocationService {
        return locationImpl
    }


    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        app: Application
    ) = LocationServices.getFusedLocationProviderClient(app)

}