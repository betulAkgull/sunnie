package com.example.weatherapp.data.model


import com.google.gson.annotations.SerializedName

data class SearchItem(
    @SerializedName("display_address")
    val displayAddress: String?,
    @SerializedName("display_place")
    val displayPlace: String?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lon")
    val lon: String?,
)