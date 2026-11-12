package com.example.androidweather.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.androidweather.presentation.components.WeatherDetailCard
import com.example.androidweather.presentation.components.WeatherSummaryCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherList(name: String) {
    val weatherViewModel = koinViewModel<WeatherViewModel>()
    val weatherState by weatherViewModel.weatherState.collectAsState()

    LaunchedEffect(name) {
        weatherViewModel.cityChanged(name)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    when {
                        weatherState.isLoading -> Text(text = "Chargement...")
                        weatherState.errorMessage != null -> {
                            Text(text = weatherState.errorMessage ?: "Erreur de chargement")
                        }
                        else -> {
                            weatherState.currentWeather?.let { currentWeather ->
                                Text(text = "Météo " + currentWeather.cityName)
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        },
        floatingActionButton = {
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when {
                weatherState.isLoading -> Text(text = "Chargement...")
                weatherState.errorMessage != null -> {
                    Text(text = weatherState.errorMessage ?: "Erreur de chargement")
                }
                else -> {
                    weatherState.currentWeather?.let { currentWeather ->
                        WeatherDetailCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                            city = currentWeather.cityName,
                            temp = currentWeather.temperatureText,
                            weatherType = currentWeather.weatherType,
                            humidity = currentWeather.humidityText
                        )
                    }

                    Text(modifier = Modifier.padding(start = 8.dp, top = 16.dp), text = "Prévisions sur 24 heures :")

                    HorizontalMultiBrowseCarousel(
                        state = rememberCarouselState { weatherState.hourlyForecast.items.count() },
                        modifier = Modifier
                            .wrapContentHeight(),
                        preferredItemWidth = 90.dp,
                        itemSpacing = 8.dp,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        userScrollEnabled = true
                    ) { i ->
                        val item = weatherState.hourlyForecast.items[i]
                        WeatherSummaryCard(
                            modifier = Modifier.fillMaxWidth(),
                            title = item.hourText,
                            temp = item.tempText,
                            humidity = item.humidityText,
                            weatherType = item.weatherType
                        )
                    }
                }
            }
        }
    }
}