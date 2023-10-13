package com.example.weatherapp.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.R
import com.example.weatherapp.common.showFullScreenPopUp
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.data.utils.LocationUtil
import com.example.weatherapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()

    private val permissionLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[LocationUtil.FINE_LOCATION] == false || permissions[LocationUtil.COARSE_LOCATION] == false) {
            showFullScreenPopUp {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
                requireActivity().finish()
            }
        } else {
            viewModel.getWeatherData()
        }
    }

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

                HomeState.LocationError -> {
                    permissionLauncher.launch(
                        arrayOf(
                            LocationUtil.FINE_LOCATION,
                            LocationUtil.COARSE_LOCATION
                        )
                    )
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