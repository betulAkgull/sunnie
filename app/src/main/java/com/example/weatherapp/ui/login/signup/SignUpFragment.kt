package com.example.weatherapp.ui.login.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.common.visible
import com.example.weatherapp.databinding.FragmentSignUpBinding
import com.example.weatherapp.ui.login.AuthState
import com.example.weatherapp.ui.login.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding(FragmentSignUpBinding::bind)
    private val viewModel by viewModels<AuthViewModel>()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
                viewModel.signInWithGoogle(credentials)
            } catch (e: ApiException) {
                // Handle sign-in failure
                e.printStackTrace()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()

        with(binding) {

            btnBack.setOnClickListener {
                findNavController().navigate(R.id.signupToSplash)
            }

            btnSignUp.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signUpUser(email, password)
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            btnSignInGoogle.setOnClickListener {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .build()

                val googleSingInClient = GoogleSignIn.getClient(requireContext(), gso)
                launcher.launch(googleSingInClient.signInIntent)
            }
            tvAlreadyUser.setOnClickListener {
                findNavController().navigate(R.id.signUpToSignIn)
            }
        }
    }

    private fun observeData() = with(binding) {

        viewModel.authState.observe(viewLifecycleOwner) { state ->

            when (state) {
                AuthState.Loading -> {
                    progressBar.visible()
                }

                is AuthState.AuthResultData -> {
                    Log.e("google signin", state.authResult.user!!.email.toString())
                    Toast.makeText(
                        requireContext(),
                        state.authResult.user!!.email.toString() + "," + state.authResult.user!!.displayName.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    findNavController().navigate(R.id.signupToHome)
                }

                is AuthState.Data -> {
                    findNavController().navigate(R.id.signupToHome)
                }

                is AuthState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
            }
        }
    }
}