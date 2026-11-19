package com.example.androidweather.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_city")
data class FavoriteCityEntity(
    @PrimaryKey val name: String
)