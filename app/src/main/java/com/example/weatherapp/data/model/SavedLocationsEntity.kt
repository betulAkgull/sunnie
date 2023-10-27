package com.example.weatherapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savedlocations")
data class SavedLocationsEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo("latitude")
    val latitude: Double,

    @ColumnInfo("longitude")
    val longitude: Double,

    @ColumnInfo("city")
    val city: String,

    @ColumnInfo("province")
    val province: String

)
