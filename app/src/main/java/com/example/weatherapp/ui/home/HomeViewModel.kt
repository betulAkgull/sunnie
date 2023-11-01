package com.example.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.DayUI
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.repository.LocationService
import com.example.weatherapp.data.repository.UserRepo
import com.example.weatherapp.data.repository.WeatherRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val repo: UserRepo,
    private val locationService: LocationService
) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    val currentUser: FirebaseUser?
        get() = repo.currentUser

    fun getWeatherData() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading

            _homeState.value = when (val locationRes = locationService.getCurrentLocation()) {

                is Resource.Success -> {
                    when (val result = weatherRepository.getWeatherData(locationRes.data)) {
                        is Resource.Success -> HomeState.WeatherList(
                            today = result.data.first(),
                            days = result.data.subList(1, 6),
                            location = locationRes.data
                        )

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

    fun getSavedLocationsData() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading

            _homeState.value = when (val savedLocations = weatherRepository.getSavedLocations()) {
                is Resource.Success -> {
                    val tempList = mutableListOf<Pair<List<DayUI>, Location>>()

                    savedLocations.data.map { location ->
                        when (val result = weatherRepository.getWeatherData(location)) {
                            is Resource.Success -> tempList.add(Pair(result.data, location))
                            is Resource.Error -> {}
                        }
                    }

                    HomeState.DrawerWeatherList(tempList)
                }

                is Resource.Error -> {
                    if (savedLocations.throwable.message == "Saved locations not found") {
                        HomeState.LocationError
                    } else {
                        HomeState.Error(savedLocations.throwable)
                    }
                }
            }
        }
    }

    fun getSavedLocationData(location: Location) {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading

            val result = weatherRepository.getWeatherData(location)
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.WeatherList(
                        today = result.data.first(),
                        days = result.data.subList(1, 6),
                        location = location
                    )
                }

                is Resource.Error -> HomeState.Error(result.throwable)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }
}


sealed interface HomeState {
    object Loading : HomeState
    data class WeatherList(val today: DayUI, val days: List<DayUI>, val location: Location) :
        HomeState

    data class DrawerWeatherList(val weatherList: List<Pair<List<DayUI>, Location>>) : HomeState
    data class Error(val throwable: Throwable) : HomeState
    object LocationError : HomeState
}