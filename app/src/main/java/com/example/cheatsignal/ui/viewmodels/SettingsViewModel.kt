package com.example.cheatsignal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatsignal.data.repository.SettingsRepository
import com.example.cheatsignal.model.Theme
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SettingsState(
    val currentTheme: Theme = Theme.SYSTEM,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val isThemeMenuExpanded: Boolean = false,
    val error: SettingsError? = null
)

sealed class SettingsError {
    data class StorageError(val message: String) : SettingsError()
    data class NetworkError(val message: String) : SettingsError()
    data class ValidationError(val message: String) : SettingsError()
}

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                // Combine both flows and update state
                repository.getThemePreference()
                    .combine(repository.getNotificationsEnabled()) { theme, notifications ->
                        _uiState.update { 
                            it.copy(
                                currentTheme = theme,
                                notificationsEnabled = notifications,
                                isLoading = false
                            )
                        }
                    }
                    .catch { e -> 
                        _uiState.update {
                            it.copy(
                                error = SettingsError.StorageError(e.message ?: "Failed to load settings"),
                                isLoading = false
                            )
                        }
                    }
                    .collect()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = SettingsError.StorageError(e.message ?: "Failed to load settings"),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            try {
                repository.setThemePreference(theme)
                _uiState.update { it.copy(currentTheme = theme) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = SettingsError.StorageError(e.message ?: "Failed to update theme"))
                }
            }
        }
    }

    fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch {
            try {
                repository.setNotificationsEnabled(enabled)
                _uiState.update { it.copy(notificationsEnabled = enabled) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = SettingsError.StorageError(e.message ?: "Failed to update notifications"))
                }
            }
        }
    }

    fun onThemeMenuExpandedChange(expanded: Boolean) {
        _uiState.update { it.copy(isThemeMenuExpanded = expanded) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
