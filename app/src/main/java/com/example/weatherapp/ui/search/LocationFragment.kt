package com.example.weatherapp.ui.search

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.common.gone
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.common.visible
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.databinding.FragmentLocationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : Fragment(R.layout.fragment_location),
    SearchCityAdapter.SearchCityListener {

    private val binding by viewBinding(FragmentLocationBinding::bind)

    private val viewModel by viewModels<SearchViewModel>()

    private val searchCityAdapter by lazy { SearchCityAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvSearch.adapter = searchCityAdapter

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    viewModel.getSearchedItems(p0.toString())
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
            })

            btnBack.setOnClickListener {
                findNavController().navigate(LocationFragmentDirections.locationToHome())
            }
        }

        observeData()
    }

    private fun observeData() {
        viewModel.searchState.observe(viewLifecycleOwner) { state ->

            when (state) {

                SearchState.Loading -> {
                    binding.progressBar.visible()
                }

                is SearchState.SearchResultList -> {
                    searchCityAdapter.submitList(state.cities)
                    binding.progressBar.gone()
                }

                is SearchState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        state.throwable.message.orEmpty(),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.gone()
                }
            }

        }

    }

    override fun onItemClick(lat: String, long: String, city: String, province: String) {
        viewModel.addToSavedLocations(Location(lat.toDouble(), long.toDouble(), city, province))
        Toast.makeText(
            requireContext(),
            "Location Saved !",
            Toast.LENGTH_SHORT
        ).show()

    }

}