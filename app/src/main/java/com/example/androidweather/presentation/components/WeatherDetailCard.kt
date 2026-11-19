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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.example.androidweather.R
import androidx.compose.ui.graphics.Color

@Composable
fun WeatherDetailCard(
    modifier: Modifier = Modifier,
    city: String,
    temp: String,
    weatherType: WeatherTypeEnum,
    humidity: String? = null,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)       // la Row prend la hauteur de son contenu le plus grand
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(2f)   // était weight(1f) : le texte prend 2/3 de la largeur
            ) {
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
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.add_favorite_description),
                        tint = Color.Red
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    WeatherLottieAnimation(
                        weatherType = weatherType,
                        modifier = Modifier.size(120.dp)
                    )
                }
            }
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