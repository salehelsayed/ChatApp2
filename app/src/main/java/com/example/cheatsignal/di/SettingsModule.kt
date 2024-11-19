package com.example.cheatsignal.di

import android.content.Context
import com.example.cheatsignal.data.repository.SettingsRepository
import com.example.cheatsignal.data.repository.SettingsRepositoryImpl

object SettingsModule {
    private var repository: SettingsRepository? = null
    private var applicationContext: Context? = null

    fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }

    fun provideSettingsRepository(context: Context): SettingsRepository {
        val appContext = applicationContext ?: context.applicationContext
        return repository ?: synchronized(this) {
            repository ?: SettingsRepositoryImpl(appContext).also {
                repository = it
            }
        }
    }
}
