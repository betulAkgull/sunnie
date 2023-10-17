package com.example.weatherapp.data.source.remote

import com.example.weatherapp.data.model.SearchItem
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CitySearchService {

    @GET()
    suspend fun getCitySearch(
        @Url url: String,
        @Query("key") apiKey: String,
        @Query("q") q: String
    ): List<SearchItem>?
}