package com.example.weatherapp.ui.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.databinding.FragmentLocationBinding

class LocationFragment : Fragment(R.layout.fragment_location) {

    private val binding by viewBinding(FragmentLocationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

        }
    }
}