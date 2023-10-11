package com.example.weatherapp.di

import android.app.Application
import com.example.weatherapp.data.source.local.LocationService
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
//
//    @Provides
//    @Singleton
//    fun provideFusedLocationProviderClient(application: Application): FusedLocationProviderClient {
//        return LocationService.getFusedLocationProviderClient(application)
//    }
//
//    @Provides
//    @Singleton
//    fun provideLocationService(fusedLocationProviderClient: FusedLocationProviderClient): LocationService {
//        return LocationService(fusedLocationProviderClient)
//    }

}