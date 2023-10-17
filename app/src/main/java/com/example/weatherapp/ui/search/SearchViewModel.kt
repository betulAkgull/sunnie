package com.example.weatherapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.model.SearchItem
import com.example.weatherapp.data.repository.CitySearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val citySearchRepository: CitySearchRepository
) : ViewModel() {

    private var _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState>
        get() = _searchState

    fun getSearchedItems(q: String) {
        viewModelScope.launch {

            _searchState.value = SearchState.Loading


            val result = citySearchRepository.getCitySearch(q)

            if (result is Resource.Success) {
                _searchState.value = SearchState.SearchResultList(result.data)
            } else if (result is Resource.Error) {
                _searchState.value = SearchState.Error(result.throwable)
            }


        }
    }


}


sealed interface SearchState {
    object Loading : SearchState
    data class SearchResultList(val cities: List<SearchItem>) : SearchState
    data class Error(val throwable: Throwable) : SearchState
}