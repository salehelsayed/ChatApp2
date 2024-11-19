package com.example.cheatsignal.domain.usecase.settings

import com.example.cheatsignal.domain.base.UseCase
import com.example.cheatsignal.domain.model.Theme
import com.example.cheatsignal.data.repository.settings.SettingsRepository

class UpdateThemeUseCase(
    private val settingsRepository: SettingsRepository
) : UseCase<Theme, Unit> {
    override suspend fun invoke(parameters: Theme) {
        settingsRepository.setThemePreference(parameters)
    }
}
