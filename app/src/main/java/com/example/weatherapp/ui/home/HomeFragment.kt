package com.example.weatherapp.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.common.setAnim
import com.example.weatherapp.common.setViewsGone
import com.example.weatherapp.common.setViewsVisible
import com.example.weatherapp.common.showFullScreenPopUp
import com.example.weatherapp.common.showToast
import com.example.weatherapp.common.toHourMinute
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.data.model.DayUI
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.utils.LocationUtil
import com.example.weatherapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),
    DrawerSavedLocationsAdapter.DrawerSavedLocationsListener {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val viewModel by viewModels<HomeViewModel>()

    private val weekWeatherAdapter by lazy { WeekWeatherAdapter() }

    private val drawerSavedLocationsAdapter by lazy { DrawerSavedLocationsAdapter(this) }

    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
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
        viewModel.getSavedLocationsData()

        observeData()

        with(binding) {

            rvWeekWeather.adapter = weekWeatherAdapter

            rvDrawerLocations.adapter = drawerSavedLocationsAdapter

            val toggle = ActionBarDrawerToggle(requireActivity(), drawerLayout, toolbar, 0, 0)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            layoutUserInfo.ivLogout.setOnClickListener {
                viewModel.logout()
                findNavController().navigate(HomeFragmentDirections.homeToSplash())
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            ivAddLocation.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.homeToLocation())
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            btnManageLocations.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.homeToSavedLocations())
            }

            viewModel.currentUser?.let {
                layoutUserInfo.tvUserEmail.text = it.email.toString()
            }

//            findNavController().setOnBackPressedDispatcher().addCallback(
//                requireActivity(),
//                object : OnBackPressedCallback(true) {
//                    override fun handleOnBackPressed() {
//                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                            drawerLayout.closeDrawer(GravityCompat.START)
//                        } else if (findNavController().previousBackStackEntry == null) {
//                            requireActivity().finish()
//                        } else {
//                            findNavController().navigateUp()
//                        }
//                    }
//                })
        }
    }

    private fun observeData() = with(binding) {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeState.Loading -> {
                    setViewsGone(
                        toolbar,
                        constraintLayout,
                        constraintlayoutSunriseSunset,
                        rvDrawerLocations
                    )
                    setViewsVisible(progressBar, progressBarDrawer)
                }

                is HomeState.DrawerWeatherList -> {
                    drawerSavedLocationsAdapter.submitList(state.weatherList)
                    setViewsGone(progressBar, progressBarDrawer)
                    setViewsVisible(
                        toolbar,
                        constraintLayout,
                        constraintlayoutSunriseSunset,
                        rvDrawerLocations
                    )
                }

                is HomeState.WeatherList -> {
                    weekWeatherAdapter.submitList(state.days)
                    setViewsGone(progressBar, progressBarDrawer)
                    setViewsVisible(
                        toolbar,
                        constraintLayout,
                        constraintlayoutSunriseSunset,
                        rvDrawerLocations
                    )
                    toolbar.title = state.location.city
                    navViewHeader.tvLocation.text = state.location.city
                    setTodayData(state.today)
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
                    setViewsGone(
                        toolbar,
                        constraintLayout,
                        constraintlayoutSunriseSunset,
                        progressBar,
                        progressBarDrawer,
                        rvDrawerLocations
                    )
                    showToast(state.throwable.message.orEmpty())
                }
            }
        }
    }

    override fun onItemClick(item: Pair<List<DayUI>, Location>) {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        viewModel.getSavedLocationData(item.second)
    }


    private fun setTodayData(day: DayUI) = with(binding) {
        ivWeather.setAnim(day.icon)
        ivWeather.playAnimation()
        tvSunriseTime.text = day.sunrise.toHourMinute()
        tvSunsetTime.text = day.sunset.toHourMinute()
        tvRainPoss.text = day.precipprob.roundToInt().toString() + "%"
        tvUv.text = "UV Index ${day.uvindex.roundToInt()?.toUVLevelString()}"
        tvMaxMin.text = "Max: ${day.tempmax.roundToInt()}° Min: ${day.tempmin.roundToInt()}° "
        tvDegree.text = day.temp.roundToInt().toString() + "\u00B0"
        tvHumidity.text = day.humidity.roundToInt().toString() + "%"
        tvWind.text = day.windspeed.roundToInt().toString() + "km/h"
        navViewHeader.ivWeather.setAnim(day.icon)
        navViewHeader.tvTemp.text = day.temp.roundToInt().toString() + "\u00B0"
    }

    private fun Int.toUVLevelString(): String {
        return when (this) {
            in 0..2 -> "Low"
            in 3..5 -> "Moderate"
            in 6..7 -> "High"
            in 8..10 -> " Very High"
            else -> "Extreme"
        }
    }


}