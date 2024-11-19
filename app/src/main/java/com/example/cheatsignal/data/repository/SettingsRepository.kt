package com.example.cheatsignal.data.repository

import com.example.cheatsignal.model.Theme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getThemePreference(): Flow<Theme>
    suspend fun setThemePreference(theme: Theme)
    suspend fun getNotificationsEnabled(): Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)
}
