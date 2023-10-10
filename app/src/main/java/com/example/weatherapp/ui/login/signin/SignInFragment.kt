package com.example.weatherapp.ui.login.signin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.databinding.FragmentSignInBinding
import com.example.weatherapp.ui.login.AuthState
import com.example.weatherapp.ui.login.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)
    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()

        with(binding) {

            btnBack.setOnClickListener {
             findNavController().navigate(R.id.signinToSplash)
            }

            btnSignIn.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signInUser(email, password)
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            tvNotRegistered.setOnClickListener {
                findNavController().navigate(R.id.signinToSignup)
            }
        }
    }

    private fun observeData() = with(binding) {

        viewModel.authState.observe(viewLifecycleOwner) { state ->

            when (state) {
                AuthState.Loading -> {
                    //binding.progressBar.visible()
                }

                is AuthState.Data -> {
                    findNavController().navigate(R.id.signinToHome)
                }

                is AuthState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
            }
        }
    }
}


