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
    val theme: ThemePreference = ThemePreference.SYSTEM,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    sealed class SettingsEvent {
        data class UpdateTheme(val theme: ThemePreference) : SettingsEvent()
        data class UpdateNotifications(val enabled: Boolean) : SettingsEvent()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.UpdateTheme -> updateTheme(event.theme)
            is SettingsEvent.UpdateNotifications -> updateNotifications(event.enabled)
        }
    }

    private fun updateTheme(theme: ThemePreference) {
        viewModelScope.launch {
            try {
                _uiState.update { 
                    it.copy(
                        isLoading = true,
                        error = null
                    )
                }
                // TODO: Implement repository call to save theme
                _uiState.update { 
                    it.copy(
                        theme = theme,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = "Failed to update theme: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch {
            try {
                _uiState.update { 
                    it.copy(
                        isLoading = true,
                        error = null
                    )
                }
                // TODO: Implement repository call to save notifications setting
                _uiState.update { 
                    it.copy(
                        notificationsEnabled = enabled,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = "Failed to update notifications: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }
}
