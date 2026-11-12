package com.example.androidweather.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.androidweather.R
import com.example.androidweather.domain.model.WeatherTypeEnum

@Composable
fun WeatherLottieAnimation(weatherType: WeatherTypeEnum, modifier: Modifier = Modifier) {
    val rawResId = when (weatherType) {
        WeatherTypeEnum.SUN -> R.raw.sun
        WeatherTypeEnum.CLOUD -> R.raw.cloud
        WeatherTypeEnum.RAIN -> R.raw.rain
        WeatherTypeEnum.THUNDERSTORM -> R.raw.cloud
        WeatherTypeEnum.SNOW -> R.raw.snow
        WeatherTypeEnum.UNKNOWN -> R.raw.cloud
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawResId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}
