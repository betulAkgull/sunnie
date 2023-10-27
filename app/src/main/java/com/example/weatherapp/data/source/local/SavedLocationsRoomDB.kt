package com.example.weatherapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.data.model.SavedLocationsEntity

@Database(entities = [SavedLocationsEntity::class], version = 1)
abstract class SavedLocationsRoomDB : RoomDatabase() {

    abstract fun savedLocationsDao(): SavedLocationsDao
}