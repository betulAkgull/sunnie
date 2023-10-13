package com.example.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.Day
import com.example.weatherapp.data.repository.LocationService
import com.example.weatherapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationService: LocationService
) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    fun getWeatherData() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading

            _homeState.value = when (val locationRes = locationService.getCurrentLocation()) {

                is Resource.Success -> {
                    when (val result = weatherRepository.getWeatherData(locationRes.data)) {
                        is Resource.Success -> HomeState.WeatherList(result.data)
                        is Resource.Error -> HomeState.Error(result.throwable)
                    }
                }

                is Resource.Error -> {
                    if (locationRes.throwable.message == "Location permission is not granted") {
                        HomeState.LocationError
                    } else {
                        HomeState.Error(locationRes.throwable)
                    }
                }
            }
        }
    }

}


sealed interface HomeState {
    object Loading : HomeState
    data class WeatherList(val days: List<Day>) : HomeState
    data class Error(val throwable: Throwable) : HomeState
    object LocationError : HomeState
}