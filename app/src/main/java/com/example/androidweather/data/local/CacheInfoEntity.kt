package com.example.androidweather.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_info")
data class CacheInfoEntity(
    @PrimaryKey val searchKey: String,
    val cachedAt: Long              // en millisecondes
)