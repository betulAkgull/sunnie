package com.example.weatherapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.common.setAnim
import com.example.weatherapp.data.model.DayUI
import com.example.weatherapp.databinding.ItemWeekdayBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt


class WeekWeatherAdapter() :
    ListAdapter<DayUI, WeekWeatherAdapter.WeakWeatherViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeakWeatherViewHolder =
        WeakWeatherViewHolder(
            ItemWeekdayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: WeakWeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WeakWeatherViewHolder(
        private val binding: ItemWeekdayBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: DayUI) = with(binding) {

            val dateTime = day.datetime
            tvDay.text = dayOfTheWeek(dateTime)
            ivWeather.setAnim(day.icon)
            tvHighDegree.text = "${day.tempmax.roundToInt()}°"
            tvLowDegree.text = "${day.tempmin.roundToInt()}°"
        }
    }

    class ProductDiffCallBack : DiffUtil.ItemCallback<DayUI>() {
        override fun areItemsTheSame(oldItem: DayUI, newItem: DayUI): Boolean {
            return oldItem.datetime == newItem.datetime
        }

        override fun areContentsTheSame(oldItem: DayUI, newItem: DayUI): Boolean {
            return oldItem == newItem
        }
    }
}

fun dayOfTheWeek(dateTime: String?): String? {
    val dayDateFormat = SimpleDateFormat(
        "yyyy-MM-dd", Locale.ENGLISH
    )
    val date = dateTime?.let { dayDateFormat.parse(it) }
    val calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
    }
    return calendar.getDisplayName(
        Calendar.DAY_OF_WEEK,
        Calendar.LONG,
        Locale.ENGLISH
    )
}