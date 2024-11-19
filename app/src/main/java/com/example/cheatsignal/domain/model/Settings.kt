package com.example.cheatsignal.domain.model

data class Settings(
    val theme: Theme = Theme.SYSTEM,
    val notificationsEnabled: Boolean = true,
    val messagePreviewEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val lastSyncTimestamp: Long = 0L
)
