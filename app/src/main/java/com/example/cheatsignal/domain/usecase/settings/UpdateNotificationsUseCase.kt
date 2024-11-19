package com.example.cheatsignal.domain.usecase.settings

import com.example.cheatsignal.domain.base.UseCase
import com.example.cheatsignal.data.repository.settings.SettingsRepository

class UpdateNotificationsUseCase(
    private val settingsRepository: SettingsRepository
) : UseCase<Boolean, Unit> {
    override suspend fun invoke(parameters: Boolean) {
        settingsRepository.setNotificationsEnabled(parameters)
    }
}
