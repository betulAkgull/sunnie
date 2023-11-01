package com.example.weatherapp.ui.savedlocation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.common.gone
import com.example.weatherapp.common.setViewsGone
import com.example.weatherapp.common.setViewsVisible
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.common.visible
import com.example.weatherapp.data.model.DayUI
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.databinding.FragmentSavedLocationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedLocationFragment : Fragment(R.layout.fragment_saved_location),
    SavedLocationsAdapter.SavedLocationsListener {

    private val binding by viewBinding(FragmentSavedLocationBinding::bind)

    private val viewModel by viewModels<SavedLocationsViewModel>()

    private val savedLocationsAdapter by lazy { SavedLocationsAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigate(SavedLocationFragmentDirections.savedLocationToHome())
            }

            rvLocation.adapter = savedLocationsAdapter
        }

        viewModel.getSavedLocationsData()
        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.savedLocationsState.observe(viewLifecycleOwner) { state ->
            when (state) {

                SavedLocationsState.Loading -> {
                    progressBar.visible()
                    setViewsGone(rvLocation, tvHeader, btnBack)
                }

                is SavedLocationsState.WeatherList -> {
                    progressBar.gone()
                    setViewsVisible(rvLocation, tvHeader, btnBack)

                    savedLocationsAdapter.submitList(state.weatherList)

                }

                is SavedLocationsState.Error -> {
                    progressBar.visible()
                    setViewsGone(rvLocation, tvHeader, btnBack)
                }

            }
        }
    }

    override fun onDeleteClick(location: Location) {
        viewModel.deleteSavedLocation(location)
        viewModel.getSavedLocationsData()
    }


}