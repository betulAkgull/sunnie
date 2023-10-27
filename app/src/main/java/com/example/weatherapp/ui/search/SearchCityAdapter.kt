package com.example.weatherapp.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.SearchItem
import com.example.weatherapp.databinding.ItemLocationBinding


class SearchCityAdapter(
    private val searchCityListener: SearchCityListener
) :
    ListAdapter<SearchItem, SearchCityAdapter.SearchCityViewHolder>(SearchItemDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCityViewHolder =
        SearchCityViewHolder(
            ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            searchCityListener
        )

    override fun onBindViewHolder(holder: SearchCityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchCityViewHolder(
        private val binding: ItemLocationBinding,
        private val searchCityListener: SearchCityListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchItem: SearchItem) = with(binding) {

            btnAdd.setOnClickListener {
                searchCityListener.onItemClick(
                    searchItem.lat!!,
                    searchItem.lon!!,
                    searchItem.displayPlace!!,
                    searchItem.displayAddress!!
                )
            }

            tvPlace.text = searchItem.displayPlace
            tvAddress.text = searchItem.displayAddress

        }
    }

    class SearchItemDiffCallBack : DiffUtil.ItemCallback<SearchItem>() {
        override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return oldItem == newItem
        }
    }

    interface SearchCityListener {
        fun onItemClick(lat: String, long: String, city: String, province: String)
    }
}

