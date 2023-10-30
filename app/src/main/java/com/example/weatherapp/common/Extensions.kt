package com.example.weatherapp.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.DialogFullPopUpBinding
import java.text.SimpleDateFormat
import java.util.Date

fun View.visible() {
    visibility = View.VISIBLE
}

fun setViewsVisible(vararg views: View) {
    views.forEach { it.visible() }
}

fun View.gone() {
    visibility = View.GONE
}

fun setViewsGone(vararg views: View) {
    views.forEach { it.gone() }
}

fun Fragment.showToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
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

fun LottieAnimationView.setAnim(weatherDesc: String?, daytime: String = "day") {
    this.setAnimation(weatherDesc?.replace("-", "_").plus(".json"))
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