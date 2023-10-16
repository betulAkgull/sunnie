package com.example.weatherapp.common

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.DialogFullPopUpBinding

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
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