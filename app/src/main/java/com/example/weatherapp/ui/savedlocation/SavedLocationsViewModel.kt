package com.example.weatherapp.ui.savedlocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.DayUI
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedLocationsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private var _savedLocationsState = MutableLiveData<SavedLocationsState>()
    val savedLocationsState: LiveData<SavedLocationsState>
        get() = _savedLocationsState


    fun getSavedLocationsData() {
        viewModelScope.launch {
            _savedLocationsState.value = SavedLocationsState.Loading

            _savedLocationsState.value =
                when (val savedLocations = weatherRepository.getSavedLocations()) {
                    is Resource.Success -> {
                        val tempList = mutableListOf<Pair<List<DayUI>, Location>>()

                        savedLocations.data.map { location ->
                            when (val result = weatherRepository.getWeatherData(location)) {
                                is Resource.Success -> tempList.add(Pair(result.data, location))
                                is Resource.Error -> {}
                            }
                        }

                        SavedLocationsState.WeatherList(tempList)
                    }

                    is Resource.Error -> {
                        SavedLocationsState.Error(savedLocations.throwable)
                    }
                }
        }
    }

    fun deleteSavedLocation(location: Location) {
        viewModelScope.launch {
            weatherRepository.removeFromSavedLocations(location)
        }
    }

}

sealed interface SavedLocationsState {
    object Loading : SavedLocationsState
    data class WeatherList(val weatherList: List<Pair<List<DayUI>, Location>>) : SavedLocationsState
    data class Error(val throwable: Throwable) : SavedLocationsState
}