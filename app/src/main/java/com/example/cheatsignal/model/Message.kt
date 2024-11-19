package com.example.cheatsignal.model

data class Message(
    val id: String,
    val content: String,
    val timestamp: Long,
    val isOutgoing: Boolean,
    val status: MessageStatus = MessageStatus.SENT,
    val replyTo: Message? = null
)

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ
}
