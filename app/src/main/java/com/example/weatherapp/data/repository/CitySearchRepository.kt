package com.example.weatherapp.data.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.common.Constants
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.SearchItem
import com.example.weatherapp.data.source.remote.CitySearchService

class CitySearchRepository(
    private val citySearchService: CitySearchService
) {

    suspend fun getCitySearch(q: String): Resource<List<SearchItem>> {
        return try {
            val result = citySearchService.getCitySearch(
                Constants.BASE_URL_SEARCH,
                BuildConfig.SEARCH_API_KEY,
                q
            )

            if (result.isNullOrEmpty()) {
                Resource.Error(Exception("error"))
            } else {
                Resource.Success(result)
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}