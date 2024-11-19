package com.example.cheatsignal.domain.model

import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val conversationId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isDelivered: Boolean = false,
    val attachments: List<Attachment> = emptyList(),
    val metadata: Map<String, String> = emptyMap()
) {
    data class Attachment(
        val id: String = UUID.randomUUID().toString(),
        val type: AttachmentType,
        val url: String,
        val size: Long,
        val name: String,
        val mimeType: String
    )

    enum class AttachmentType {
        IMAGE,
        VIDEO,
        AUDIO,
        DOCUMENT,
        OTHER
    }

    companion object {
        const val MAX_CONTENT_LENGTH = 2000
        const val MAX_ATTACHMENTS = 10
    }
}
