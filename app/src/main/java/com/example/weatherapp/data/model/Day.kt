package com.example.weatherapp.data.model


import com.google.gson.annotations.SerializedName

data class Day(
    @SerializedName("datetime")
    val datetime: String?,
    @SerializedName("humidity")
    val humidity: Double?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("precipprob")
    val precipprob: Double?,
    @SerializedName("sunrise")
    val sunrise: String?,
    @SerializedName("sunset")
    val sunset: String?,
    @SerializedName("temp")
    val temp: Double?,
    @SerializedName("tempmax")
    val tempmax: Double?,
    @SerializedName("tempmin")
    val tempmin: Double?,
    @SerializedName("uvindex")
    val uvindex: Double?,
    @SerializedName("windspeed")
    val windspeed: Double?
)