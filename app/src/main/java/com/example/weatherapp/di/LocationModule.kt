package com.example.weatherapp.di

import android.app.Application
import com.example.weatherapp.data.repository.LocationImpl
import com.example.weatherapp.data.source.local.LocationService
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class LocationModule {

    @Binds
    abstract fun bindLocationTrackerService(location: LocationImpl): LocationService

    companion object {
        @Provides
        @Singleton
        fun provideFusedLocationProviderClient(
            app: Application
        ) = LocationServices.getFusedLocationProviderClient(app)
    }
}