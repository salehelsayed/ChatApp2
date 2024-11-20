package com.example.cheatsignal.ui.state

import com.example.cheatsignal.domain.model.Theme

/**
 * Represents the UI state for settings screen
 */
data class SettingsState(
    val theme: Theme = Theme.SYSTEM,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    companion object {
        val Empty = SettingsState()
    }
}
