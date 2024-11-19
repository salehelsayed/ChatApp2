package com.example.cheatsignal.domain.usecase.settings

import com.example.cheatsignal.domain.base.NoParamFlowUseCase
import com.example.cheatsignal.data.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationsUseCase(
    private val settingsRepository: SettingsRepository
) : NoParamFlowUseCase<Boolean> {
    override suspend fun invoke(): Flow<Boolean> = settingsRepository.getNotificationsEnabled()
}
