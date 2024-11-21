package com.example.cheatsignal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "communal_addresses")
data class CommunalAddress(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val addressLine: String,
    val locality: String,
    val postalCode: String,
    val country: String,
    val lastModified: Long = System.currentTimeMillis()
)
