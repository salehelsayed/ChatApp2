package com.example.cheatsignal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "hashtags")
data class Hashtag(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val tag: String,
    val usageCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUsed: Long = System.currentTimeMillis()
)
