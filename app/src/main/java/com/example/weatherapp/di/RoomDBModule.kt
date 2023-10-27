package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.data.source.local.SavedLocationsRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, SavedLocationsRoomDB::class.java, "saved_locations_room_db")
            .build()


    @Provides
    @Singleton
    fun provideSavedLocationsDao(roomDB: SavedLocationsRoomDB) = roomDB.savedLocationsDao()
}