package com.example.weatherapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.Day
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.source.local.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationService: LocationService
) :
    ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState


    fun getWeatherData() {
        viewModelScope.launch {

            val locationRes = locationService.getCurrentLocation()

            val location = when (locationRes) {
                is Resource.Success -> {
                    locationRes.data
                }

                is Resource.Error -> {
                    Location(40.7128, -74.0060)
                }
            }

            _homeState.value = HomeState.Loading

            val result = weatherRepository.getWeatherData(location)
            Log.e("viewmodel", result.toString())
            if (result is Resource.Success) {
                _homeState.value = HomeState.WeatherList(result.data)
            } else if (result is Resource.Error) {
                _homeState.value = HomeState.Error(result.throwable)
            }

        }
    }


}


sealed interface HomeState {
    object Loading : HomeState
    data class WeatherList(val days: List<Day>) : HomeState
    data class Error(val throwable: Throwable) : HomeState
}