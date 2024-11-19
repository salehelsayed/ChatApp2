package com.example.cheatsignal.di

import android.content.Context
import com.example.cheatsignal.data.repository.SettingsRepository
import com.example.cheatsignal.data.repository.SettingsRepositoryImpl

object SettingsModule {
    private var repository: SettingsRepository? = null

    fun provideSettingsRepository(context: Context): SettingsRepository {
        return repository ?: synchronized(this) {
            repository ?: SettingsRepositoryImpl(context.applicationContext).also {
                repository = it
            }
        }
    }
}
