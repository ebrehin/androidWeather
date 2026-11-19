package com.example.androidweather.data.mapper

import com.example.androidweather.data.local.FavoriteCityEntity
import com.example.androidweather.domain.model.FavoriteCityModel

fun FavoriteCityEntity.toModel() = FavoriteCityModel(name = name)