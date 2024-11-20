package com.example.cheatsignal.data

import java.util.UUID

data class Conversation(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val lastMessage: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0,
    val messages: List<Message> = emptyList(),
    val isAI: Boolean = false,
    val isCurrentlyViewed: Boolean = false
)
