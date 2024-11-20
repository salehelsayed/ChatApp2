package com.example.cheatsignal.di

import android.content.Context
import com.example.cheatsignal.data.source.local.datastore.SettingsDataStore
import com.example.cheatsignal.data.OpenAIService
import com.example.cheatsignal.data.ConversationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): SettingsDataStore = SettingsDataStore(context)

    @Provides
    @Singleton
    fun provideOpenAIService(
        @ApplicationContext context: Context
    ): OpenAIService = OpenAIService(context)

    @Provides
    @Singleton
    fun provideConversationRepository(
        openAIService: OpenAIService
    ): ConversationRepository = ConversationRepository(openAIService)
}
