package com.example.weatherapp.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.databinding.DialogFullPopUpBinding
import java.util.Locale

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}


fun Context.getCityName(location: Location): String {
    var cityName: String = "null"
    val geoCoder = Geocoder(this, Locale.getDefault())

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geoCoder.getFromLocation(
            location.latitude,
            location.longitude,
            3,
            Geocoder.GeocodeListener {
                cityName = it[0].locality.toString()
            })
    } else {
        val adress = geoCoder.getFromLocation(location.latitude, location.longitude, 3)
        cityName = adress!![0].locality.toString()
    }


    return cityName
}

fun Fragment.showFullScreenPopUp(
    onButtonClickListener: (() -> Unit)? = null,
) {
    val dialog = Dialog(requireContext(), R.style.BaseDialogStyle)
    val binding = DialogFullPopUpBinding.inflate(dialog.layoutInflater, null, false)
    dialog.setContentView(binding.root)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    dialog.window?.statusBarColor = resources.getColor(R.color.white, null)

    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)

    binding.btnOkay.setOnClickListener {
        dialog.dismiss()
        onButtonClickListener?.invoke()
    }

    if (!requireActivity().isFinishing) {
        dialog.show()
    }
}