package com.example.weatherapp.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.common.getWeatherTypeByDesc
import com.example.weatherapp.common.gone
import com.example.weatherapp.common.isDeviceTimeEarlier
import com.example.weatherapp.common.showFullScreenPopUp
import com.example.weatherapp.common.toHourMinute
import com.example.weatherapp.common.toUVLevelString
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.common.visible
import com.example.weatherapp.data.utils.LocationUtil
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()
    private val viewModelAuth by viewModels<AuthViewModel>()
    private val weekWeatherAdapter by lazy { WeekWeatherAdapter() }

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

    override fun onPause() {
        super.onPause()
        Log.i("pause", "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.i("resume", "onResume")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getWeatherData()

        observeData()

        with(binding) {

            rvWeekWeather.adapter = weekWeatherAdapter

            val toggle = ActionBarDrawerToggle(requireActivity(), drawerLayout, toolbar, 0, 0)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()


            layoutUserInfo.ivLogout.setOnClickListener {
                viewModelAuth.logout()
                findNavController().navigate(HomeFragmentDirections.homeToSplash())
                drawerLayout.closeDrawer(GravityCompat.START)
            }


            ivAddLocation.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.homeToLocation())
                drawerLayout.closeDrawer(GravityCompat.START)
            }


            viewModelAuth.currentUser?.let {
                layoutUserInfo.tvUserEmail.text = viewModelAuth.currentUser?.email.toString()
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

    private fun observeData() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeState.Loading -> {
                    binding.toolbar.gone()
                    binding.constraintLayout.gone()
                    binding.constraintLayout.gone()
                    binding.constraintlayoutSunriseSunset.gone()
                    binding.progressBar.visible()
                }

                is HomeState.WeatherList -> {
                    weekWeatherAdapter.submitList(state.days.subList(1, 6))
                    with(binding) {
                        progressBar.gone()
                        toolbar.visible()
                        binding.constraintLayout.visible()
                        binding.constraintLayout.visible()
                        binding.constraintlayoutSunriseSunset.visible()
                        ivSunrise.setAnimation("sunrise.json")
                        ivSunrise.playAnimation()
                        ivSunset.setAnimation("sunset.json")
                        ivSunset.playAnimation()
                        ivWeather.setAnimation(
                            getWeatherTypeByDesc(
                                false,
                                state.days[0].sunset.toString().toHourMinute()
                                    .isDeviceTimeEarlier(), state.days[0].icon
                            )
                        )
                        ivWeather.playAnimation()
                        tvSunriseTime.text = state.days[0].sunrise.toString().toHourMinute()
                        tvSunsetTime.text = state.days[0].sunset.toString().toHourMinute()
                        tvRainPoss.text =
                            state.days[0].precipprob?.roundToInt().toString() + "%"
                        tvUv.text =
                            "UV Index ${state.days[0].uvindex?.roundToInt()?.toUVLevelString()}"
                        tvMaxMin.text = "Max: ${
                            state.days[0].tempmax?.roundToInt().toString()
                        }°   Min: ${state.days[0].tempmin?.roundToInt().toString()}° "
                        tvDegree.text = state.days[0].temp?.roundToInt().toString() + "\u00B0"
                        tvHumidity.text = state.days[0].humidity?.roundToInt().toString() + "%"
                        tvWind.text = state.days[0].windspeed?.roundToInt().toString() + "km/h"
                        tvLocation.text = state.location.province

                        navViewHeader.ivWeather.setAnimation(
                            getWeatherTypeByDesc(
                                false,
                                state.days[0].sunset.toString().toHourMinute()
                                    .isDeviceTimeEarlier(), state.days[0].icon
                            )
                        )
                        navViewHeader.ivWeather.playAnimation()
                        navViewHeader.tvLocation.text = state.location.city
                        navViewHeader.tvTemp.text =
                            state.days[0].temp?.roundToInt().toString() + "\u00B0"
                    }
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
                    binding.toolbar.gone()
                    binding.constraintLayout.gone()
                    binding.constraintLayout.gone()
                    binding.constraintlayoutSunriseSunset.gone()
                    binding.progressBar.gone()
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