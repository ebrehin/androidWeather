package com.example.androidweather.presentation.screen.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.androidweather.presentation.components.ErrorCard
import com.example.androidweather.presentation.components.WeatherDetailCard
import com.example.androidweather.presentation.components.DailyWeatherCard
import com.example.androidweather.presentation.components.WeatherSummaryCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigateToDetail: (String, String, Double?, Double?) -> Unit) {
    val viewModel = koinViewModel<HomeViewModel>()
    val weatherState by viewModel.weatherState.collectAsState()
    var cityName by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onPermissionGranted()
        } else {
            viewModel.onPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        if (weatherState.search != HomeUiState.SearchCriteria()) {
            viewModel.refresh()
            return@LaunchedEffect
        }

        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.onPermissionGranted()
        } else {
            locationPermissionLauncher.launch(permission)
        }
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
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = cityName,
                onValueChange = { cityName = it },
                label = { Text("Ville") },
                placeholder = { Text("Rechercher une ville") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    cityName.trim().takeIf { it.isNotEmpty() }?.let { city ->
                        viewModel.cityChanged(city)
                        keyboardController?.hide()
                    }
                })
            )
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
                            weatherType = item.weatherType,
                        )
                    }

                    Text(
                        modifier = Modifier.padding(start = 8.dp, top = 16.dp),
                        text = "Prévisions sur 5 jours :"
                    )

                    when {
                        weatherState.dailyForecast.isLoading -> {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = "Chargement des prévisions journalières..."
                        )
                        }
                        weatherState.dailyForecast.errorMessage != null -> {
                            ErrorCard(message = weatherState.dailyForecast.errorMessage!!)
                        }
                        else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            weatherState.dailyForecast.items.forEach { item ->
                                DailyWeatherCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    day = item.dayText,
                                    minTemperature = item.minTemperatureText,
                                    maxTemperature = item.maxTemperatureText,
                                    weatherType = item.weatherType,
                                    onClick = {
                                        val search = weatherState.search
                                        onNavigateToDetail(
                                            search.city.orEmpty(),
                                            item.dateString,
                                            search.lat,
                                            search.lon
                                        )
                                    }
                                )
                            }
                        }
                        }
                    }
                }
            }
        }
    }
}
