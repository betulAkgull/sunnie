package com.example.weatherapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.repository.LocationImpl
import com.example.weatherapp.data.repository.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationServiceViewModel @Inject constructor(
    private val locationImpl: LocationImpl
) :
    ViewModel() {

    private var _locationState = MutableLiveData<LocationServiceState>()
    val locationState: LiveData<LocationServiceState>
        get() = _locationState


    fun getLocationData() {

        viewModelScope.launch {

            _locationState.value = LocationServiceState.Loading

            val result = locationImpl.getCurrentLocation()

            if (result is Resource.Success) {
                _locationState.value = LocationServiceState.LocationData(result.data)

            } else if (result is Resource.Error) {
                _locationState.value = LocationServiceState.Error(Exception("Error"))
            }
        }
    }


    }





sealed interface LocationServiceState {
    object Loading : LocationServiceState
    data class LocationData(val location: Location) : LocationServiceState
    data class Error(val throwable: Throwable) : LocationServiceState
}