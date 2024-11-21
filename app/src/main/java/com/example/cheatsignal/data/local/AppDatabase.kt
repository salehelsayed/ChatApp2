package com.example.cheatsignal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cheatsignal.data.model.CommunalAddress
import com.example.cheatsignal.data.model.Hashtag
import com.example.cheatsignal.data.model.JobSkill

@Database(
    entities = [
        CommunalAddress::class,
        JobSkill::class,
        Hashtag::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(SyndicalConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun communalDao(): CommunalDao
    abstract fun syndicalDao(): SyndicalDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create jobs_skills table
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS jobs_skills (
                        id TEXT PRIMARY KEY NOT NULL,
                        title TEXT NOT NULL,
                        type TEXT NOT NULL,
                        description TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        lastModified INTEGER NOT NULL
                    )
                """)
                
                // Create hashtags table
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS hashtags (
                        id TEXT PRIMARY KEY NOT NULL,
                        tag TEXT NOT NULL,
                        usageCount INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL,
                        lastUsed INTEGER NOT NULL
                    )
                """)
            }
        }
    }
}
