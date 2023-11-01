package com.example.weatherapp.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.model.SavedLocationsEntity

@Dao
interface SavedLocationsDao {

    @Query("SELECT * FROM savedlocations")
    suspend fun getSavedLocations(): List<SavedLocationsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToSavedLocations(savedLocation: SavedLocationsEntity)

    @Query("DELETE FROM savedlocations WHERE latitude = :latitude and longitude =:longitude")
    suspend fun removeFromSavedLocations(latitude: Double, longitude: Double)

}