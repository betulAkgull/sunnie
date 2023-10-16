package com.example.weatherapp.common

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.DialogFullPopUpBinding
import java.text.SimpleDateFormat
import java.util.Date

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}


fun String?.isDeviceTimeEarlier(): String {
    val inputFormat = SimpleDateFormat("HH:mm")
    val currentTime = Date()

    if (this != null) {
        try {
            val targetTime = inputFormat.parse(this)
            if (targetTime != null && currentTime.before(targetTime)) {
                return "day"
            }
        } catch (e: Exception) {
            // Handle parsing errors if the input string is not in HH:mm format
            // You can return an error message or handle it as needed
        }
    }

    return "night"
}


fun getWeatherTypeByDesc(isAdapter: Boolean, daytime: String, weatherDesc: String?): String {
    return when (weatherDesc) {
        "clear-day" -> if (isAdapter || daytime == "day") "clear_day.json" else "clear_night.json"
        "clear-night" -> if (isAdapter || daytime == "day") "clear_day.json" else "clear_night.json"
        "cloudy" -> "cloudy.json"
        "partly-cloudy-day" -> if (isAdapter || daytime == "day") "partly_cloudy_day.json" else "partly_cloudy_night.json"
        "partly-cloudy-night" -> if (isAdapter || daytime == "day") "partly_cloudy_day.json" else "partly_cloudy_night.json"
        "wind" -> "wind.json"
        "showers-night" -> if (isAdapter || daytime == "day") "showers_day.json" else "showers_night.json"
        "showers-day" -> if (isAdapter || daytime == "day") "showers_day.json" else "showers_night.json"
        "rain" -> "rain.json"
        "thunder-showers-night" -> if (isAdapter || daytime == "day") "thunder_showers_day.json" else "thunder_showers_night.json"
        "thunder-showers-day" -> if (isAdapter || daytime == "day") "thunder_showers_day.json" else "thunder_showers_night.json"
        "thunder-rain" -> "thunder.json"
        "snow-showers-day" -> if (isAdapter || daytime == "day") "snow_showers_day.json" else "snow_showers_night.json"
        "snow-showers-night" -> if (isAdapter || daytime == "day") "snow_showers_day.json" else "snow_showers_night.json"
        "snow" -> "snow.json"
        else -> throw IllegalArgumentException("Geçersiz hava durumu tanımı: $weatherDesc")
    }
}

fun Int.toUVLevelString(): String {
    return when (this) {
        in 0..2 -> "Low"
        in 3..5 -> "Moderate"
        in 6..7 -> "High"
        in 8..10 -> " Very High"
        else -> "Extreme"
    }
}

fun String.toHourMinute(): String {
    val parts = this.split(":")
    if (parts.size == 3) {
        val hour = parts[0]
        val minute = parts[1]
        return "$hour:$minute"
    }
    return this // Return the original string if it doesn't match the format "HH:MM:SS"
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