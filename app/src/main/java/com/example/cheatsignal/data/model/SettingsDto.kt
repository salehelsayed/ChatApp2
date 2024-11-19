package com.example.cheatsignal.data.model

data class SettingsDto(
    val theme: String = "SYSTEM",
    val notificationsEnabled: Boolean = true,
    val messagePreviewEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val lastSyncTimestamp: Long = 0L,
    val version: Int = 1
)
