package com.example.weatherapp.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.R
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getWeatherData()
        observeData()

        with(binding) {

        }
    }

    private fun observeData() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeState.Loading -> {
                    // binding.progressBarHome.visible()
                }

                is HomeState.WeatherList -> {
                    //productsAdapter.submitList(state.products)
                    // binding.progressBarHome.invisible()
                    binding.tvDegree.text = state.days[0].temp.toString() + "\u00B0"
                    binding.tvHumidity.text = state.days[0].humidity.toString()
                    binding.tvWind.text = state.days[0].windspeed.toString()
                }


                is HomeState.Error -> {
                    //binding.progressBarHome.invisible()
                    Toast.makeText(
                        requireContext(),
                        state.throwable.message.orEmpty(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}