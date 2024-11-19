package com.example.cheatsignal.model

data class Conversation(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: Long,
    val avatarUrl: String? = null,
    val unreadCount: Int = 0
)
