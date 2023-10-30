package com.example.weatherapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.common.setAnim
import com.example.weatherapp.data.model.Day
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.databinding.ItemDrawerBinding
import kotlin.math.roundToInt

class DrawerSavedLocationsAdapter(
    private val drawerSavedLocationsListener: DrawerSavedLocationsAdapter.DrawerSavedLocationsListener
) :
    ListAdapter<Pair<List<Day>, Location>, DrawerSavedLocationsAdapter.DrawerSavedLocationsViewHolder>(
        SavedLocationsDiffCallBack()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DrawerSavedLocationsViewHolder =
        DrawerSavedLocationsViewHolder(
            ItemDrawerBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            drawerSavedLocationsListener
        )

    override fun onBindViewHolder(holder: DrawerSavedLocationsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DrawerSavedLocationsViewHolder(
        private val binding: ItemDrawerBinding,
        private val drawerSavedLocationsListener: DrawerSavedLocationsListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pair<List<Day>, Location>) {
            binding.tvLocation.text = item.second.city
            item.first.forEach {
                binding.tvTemp.text = "${it.temp?.roundToInt().toString()}°"
                binding.ivWeather.setAnim(it.icon)
            }

            binding.root.setOnClickListener {
                drawerSavedLocationsListener.onItemClick(item)
            }
        }
    }

    class SavedLocationsDiffCallBack : DiffUtil.ItemCallback<Pair<List<Day>, Location>>() {
        override fun areItemsTheSame(
            oldItem: Pair<List<Day>, Location>,
            newItem: Pair<List<Day>, Location>
        ): Boolean {
            return oldItem.second == newItem.second
        }

        override fun areContentsTheSame(
            oldItem: Pair<List<Day>, Location>,
            newItem: Pair<List<Day>, Location>
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface DrawerSavedLocationsListener {
        fun onItemClick(item: Pair<List<Day>, Location>)
    }
}