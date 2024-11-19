package com.example.cheatsignal.domain.base

import kotlinx.coroutines.flow.Flow

interface FlowUseCase<in P, R> {
    suspend operator fun invoke(parameters: P): Flow<R>
}

interface NoParamFlowUseCase<R> {
    suspend operator fun invoke(): Flow<R>
}

interface UseCase<in P, R> {
    suspend operator fun invoke(parameters: P): R
}

interface NoParamUseCase<R> {
    suspend operator fun invoke(): R
}
