package com.example.weatherapp.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.databinding.FragmentSplashBinding
import com.example.weatherapp.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)
    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentUser?.let {
            findNavController().navigate(R.id.homeFragment)
        }

        with(binding) {

            btnSignin.setOnClickListener {
                findNavController().navigate(R.id.splashToSignin)
            }

            btnSignup.setOnClickListener {
                findNavController().navigate(R.id.splashToSignup)
            }
        }

    }
}