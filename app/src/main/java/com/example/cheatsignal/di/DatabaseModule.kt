package com.example.cheatsignal.di

import android.content.Context
import androidx.room.Room
import com.example.cheatsignal.data.local.AppDatabase
import com.example.cheatsignal.data.local.CommunalDao
import com.example.cheatsignal.data.local.SyndicalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cheat_signal_db"
        )
        .addMigrations(AppDatabase.MIGRATION_1_2)
        .build()
    }

    @Provides
    fun provideCommunalDao(appDatabase: AppDatabase): CommunalDao {
        return appDatabase.communalDao()
    }

    @Provides
    fun provideSyndicalDao(appDatabase: AppDatabase): SyndicalDao {
        return appDatabase.syndicalDao()
    }
}
