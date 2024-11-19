package com.example.cheatsignal.domain.usecase.settings

import com.example.cheatsignal.domain.base.NoParamFlowUseCase
import com.example.cheatsignal.domain.model.Theme
import com.example.cheatsignal.data.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetThemeUseCase(
    private val settingsRepository: SettingsRepository
) : NoParamFlowUseCase<Theme> {
    override suspend fun invoke(): Flow<Theme> = settingsRepository.getThemePreference()
}
