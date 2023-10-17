package com.example.weatherapp.di

import com.example.weatherapp.data.repository.CitySearchRepository
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.source.remote.CitySearchService
import com.example.weatherapp.data.source.remote.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesWeatherRepository(
        weatherService: WeatherService
    ): WeatherRepository = WeatherRepository(weatherService)


    @Provides
    @Singleton
    fun providesCitySearchRepository(
        citySearchService: CitySearchService
    ): CitySearchRepository = CitySearchRepository(citySearchService)
}