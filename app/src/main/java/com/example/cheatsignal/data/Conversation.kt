package com.example.cheatsignal.data

data class Conversation(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: Long,
    val unreadCount: Int = 0,
    val isAI: Boolean = false,
    val isCurrentlyViewed: Boolean = false,
    val messages: List<Message> = emptyList()
)
