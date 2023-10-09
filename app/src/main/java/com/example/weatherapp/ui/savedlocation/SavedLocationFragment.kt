package com.example.weatherapp.ui.savedlocation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.databinding.FragmentSavedLocationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedLocationFragment : Fragment(R.layout.fragment_saved_location) {

    private val binding by viewBinding(FragmentSavedLocationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

        }
    }
}