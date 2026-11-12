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
fun WeatherAnimation(
    weatherType: WeatherTypeEnum,
    modifier: Modifier = Modifier
) {
    when (weatherType) {
        WeatherTypeEnum.SUN -> {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.sun)
            )

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

        WeatherTypeEnum.CLOUD -> {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.cloudy)
            )

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

        WeatherTypeEnum.RAIN -> {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.rain)
            )

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

        WeatherTypeEnum.THUNDERSTORM -> {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.rain)
            )

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

        WeatherTypeEnum.SNOW -> {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.snow)
            )

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

        else -> {
            // Autres animations
        }
    }
}