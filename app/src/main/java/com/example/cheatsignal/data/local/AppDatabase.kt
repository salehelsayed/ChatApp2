package com.example.cheatsignal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cheatsignal.data.model.CommunalAddress

@Database(
    entities = [CommunalAddress::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun communalDao(): CommunalDao
}
