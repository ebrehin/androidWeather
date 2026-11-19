package com.example.androidweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidweather.domain.usecase.ObserveThemeModeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val observeThemeModeUseCase: ObserveThemeModeUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(MainUiState())
    val viewState: StateFlow<MainUiState> = _viewState.asStateFlow()

    init {
        observeThemeMode()
    }

    // On écoute la préférence de thème : à chaque changement, on met à jour le state
    private fun observeThemeMode() {
        viewModelScope.launch {
            observeThemeModeUseCase().collect { themeMode ->
                _viewState.update { it.copy(isLoading = false, themeMode = themeMode) }
            }
        }
    }
}