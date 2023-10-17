package com.example.weatherapp.data.source.remote

import com.example.weatherapp.data.model.response.GetWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {


    @GET("{latitude},{longitude}")
    suspend fun getWeatherData(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double,
        @Query("unitGroup") unitGroup: String,
        @Query("elements") elements: String,
        @Query("include") include: String,
        @Query("key") apiKey: String,
        @Query("contentType") contentType: String
    ): GetWeatherResponse


}