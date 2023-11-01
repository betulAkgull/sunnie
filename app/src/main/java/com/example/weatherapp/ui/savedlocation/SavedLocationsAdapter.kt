package com.example.weatherapp.ui.savedlocation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.common.toTitleCase
import com.example.weatherapp.data.model.DayUI
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.databinding.ItemSavedLocationBinding
import kotlin.math.roundToInt

class SavedLocationsAdapter(
    private val savedLocationsListener: SavedLocationsAdapter.SavedLocationsListener
) :
    ListAdapter<Pair<List<DayUI>, Location>, SavedLocationsAdapter.SavedLocationsViewHolder>(
        SavedLocationsDiffCallBack()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedLocationsViewHolder =
        SavedLocationsViewHolder(
            ItemSavedLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            savedLocationsListener
        )

    override fun onBindViewHolder(holder: SavedLocationsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SavedLocationsViewHolder(
        private val binding: ItemSavedLocationBinding,
        private val savedLocationsListener: SavedLocationsListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pair<List<DayUI>, Location>) {
            binding.tvLocationName.text = item.second.city
            item.first.forEach {
                binding.tvWeatherType.text = it.icon.toTitleCase()
                binding.tvDegree.text = "${it.temp.roundToInt()}°"
                binding.tvHighDegree.text = "${it.tempmax.roundToInt()}°"
                binding.tvLowDegree.text = "${it.tempmin.roundToInt()}°"
            }

            binding.btnDelete.setOnClickListener {
                savedLocationsListener.onDeleteClick(item.second)
            }

        }
    }

    class SavedLocationsDiffCallBack : DiffUtil.ItemCallback<Pair<List<DayUI>, Location>>() {
        override fun areItemsTheSame(
            oldItem: Pair<List<DayUI>, Location>,
            newItem: Pair<List<DayUI>, Location>
        ): Boolean {
            return oldItem.second == newItem.second
        }

        override fun areContentsTheSame(
            oldItem: Pair<List<DayUI>, Location>,
            newItem: Pair<List<DayUI>, Location>
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface SavedLocationsListener {
        fun onDeleteClick(location: Location)
    }
}