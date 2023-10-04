package com.example.weatherapp.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding(FragmentSignUpBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

        }
    }
}