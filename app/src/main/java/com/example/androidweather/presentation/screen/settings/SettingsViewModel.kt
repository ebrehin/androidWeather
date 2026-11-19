package com.example.androidweather.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidweather.domain.model.ThemeMode
import com.example.androidweather.domain.usecase.ObserveThemeModeUseCase
import com.example.androidweather.domain.usecase.SetThemeModeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val observeThemeModeUseCase: ObserveThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(SettingsUiState())
    val viewState: StateFlow<SettingsUiState> = _viewState.asStateFlow()

    init {
        observeThemeMode()
    }

    private fun observeThemeMode() {
        viewModelScope.launch {
            observeThemeModeUseCase().collect { themeMode ->
                _viewState.update { it.copy(isLoading = false, themeMode = themeMode) }
            }
        }
    }

    fun onThemeModeSelected(themeMode: ThemeMode) {
        viewModelScope.launch {
            setThemeModeUseCase(themeMode)
        }
    }
}