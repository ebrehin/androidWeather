package com.example.androidweather.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidweather.domain.model.WeatherTypeEnum

@Composable
fun WeatherDetailCard(
    modifier: Modifier = Modifier,
    city: String,
    temp: String,
    weatherType: WeatherTypeEnum,
    humidity: String? = null,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
              .padding(16.dp)
              .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(fontWeight = FontWeight.Bold, fontSize = 24.sp, text = city)
                Text(fontWeight = FontWeight.Bold, fontSize = 42.sp, text = temp)
                var typeText = ""
                weatherType.let { item ->
                    when (item) {
                        WeatherTypeEnum.SUN -> {
                            typeText = "Ensoleillé"
                        }
                        WeatherTypeEnum.CLOUD -> {
                            typeText = "Nuageux"
                        }
                        WeatherTypeEnum.RAIN -> {
                            typeText = "Pluvieux"
                        }
                        WeatherTypeEnum.THUNDERSTORM -> {
                            typeText = "Orageux"
                        }
                        WeatherTypeEnum.SNOW -> {
                            typeText = "Neigeux"
                        }
                        else -> {
                            typeText = ""
                        }
                    }
                }
                Text(fontWeight = FontWeight.Bold, fontSize = 20.sp, text = typeText)
                humidity?.let { Text(fontWeight = FontWeight.Bold, fontSize = 20.sp, text = it) }
            }
            WeatherAnimation(
                weatherType = weatherType,
                modifier = Modifier.size(120.dp)
            )
        }
    }
}

@Composable
fun DailyWeatherCard(
    modifier: Modifier = Modifier,
    day: String,
    minTemperature: String,
    maxTemperature: String,
    weatherType: WeatherTypeEnum,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable { onClick() },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(fontWeight = FontWeight.Bold, fontSize = 30.sp, text = day)
                    Text(fontWeight = FontWeight.Bold, fontSize = 20.sp, text = minTemperature)
                }
                Text(fontWeight = FontWeight.Bold, fontSize = 20.sp, text = maxTemperature)
            }
            WeatherAnimation(
                weatherType = weatherType,
                modifier = Modifier.size(120.dp)
            )
        }
    }
}