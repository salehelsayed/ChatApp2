package com.example.cheatsignal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatsignal.ui.screens.ThemePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsState(
    val themePreference: ThemePreference = ThemePreference.SYSTEM,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = true,
    val error: String? = null
)

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                // Start loading
                _uiState.update { it.copy(isLoading = true) }

                // For now, just simulate loading
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        themePreference = ThemePreference.SYSTEM,
                        notificationsEnabled = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load settings: ${e.message}"
                    )
                }
            }
        }
    }

    fun updateTheme(theme: ThemePreference) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(themePreference = theme) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Failed to update theme: ${e.message}")
                }
            }
        }
    }

    fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(notificationsEnabled = enabled) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Failed to update notifications: ${e.message}")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
