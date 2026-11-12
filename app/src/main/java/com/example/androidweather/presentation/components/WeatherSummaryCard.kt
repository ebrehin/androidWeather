package com.example.androidweather.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidweather.domain.model.WeatherTypeEnum

@Composable
fun WeatherSummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    temp: String,
    humidity: String,
    weatherType: WeatherTypeEnum,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ){
        Column(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeatherAnimation(
                weatherType = weatherType,
                modifier = Modifier.size(56.dp)
            )
            Text(modifier = Modifier.padding(top = 10.dp), fontWeight = FontWeight.Bold, text = title)
            Text(text = temp)
            Text(text = humidity)
        }
    }
}