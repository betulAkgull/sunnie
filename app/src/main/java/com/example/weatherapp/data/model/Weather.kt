package com.example.weatherapp.data.model


import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("address")
    val address: String?,
    @SerializedName("days")
    val days: List<Day?>?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("queryCost")
    val queryCost: Int?,
    @SerializedName("resolvedAddress")
    val resolvedAddress: String?,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("tzoffset")
    val tzoffset: Double?
)