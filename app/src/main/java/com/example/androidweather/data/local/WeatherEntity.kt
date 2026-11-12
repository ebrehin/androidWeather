package com.example.androidweather.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidweather.domain.model.WeatherTypeEnum

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val searchKey: String,          // la recherche à laquelle appartient la ligne (ex: "city:paris")
    // Ville
    val cityName: String,
    val lat: Double?,
    val lon: Double?,
    val population: Int,
    val sunrise: Double?,
    val sunset: Double?,
    // Prévision
    val date: String,               // format ISO : "2026-11-11T15:00"
    val weatherType: WeatherTypeEnum,
    val temp: Double,
    val humidity: Double,
    val pressure: Double
)