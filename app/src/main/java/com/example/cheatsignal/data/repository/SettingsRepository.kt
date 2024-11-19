package com.example.cheatsignal.data.repository

import com.example.cheatsignal.ui.screens.ThemePreference
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getThemePreference(): Flow<ThemePreference>
    suspend fun setThemePreference(theme: ThemePreference)
    suspend fun getNotificationsEnabled(): Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)
}
