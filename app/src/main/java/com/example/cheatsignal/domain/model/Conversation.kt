package com.example.cheatsignal.domain.model

import java.util.UUID

data class Conversation(
    val id: String = UUID.randomUUID().toString(),
    val participants: List<Participant>,
    val lastMessage: Message? = null,
    val unreadCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val type: ConversationType = ConversationType.DIRECT,
    val metadata: Map<String, String> = emptyMap()
) {
    data class Participant(
        val id: String,
        val name: String,
        val profilePictureUrl: String? = null,
        val role: Role = Role.MEMBER,
        val lastSeen: Long = System.currentTimeMillis(),
        val isOnline: Boolean = false
    )

    enum class Role {
        OWNER,
        ADMIN,
        MEMBER
    }

    enum class ConversationType {
        DIRECT,
        GROUP,
        BROADCAST
    }

    companion object {
        const val MAX_GROUP_PARTICIPANTS = 100
        const val MAX_BROADCAST_RECIPIENTS = 256
    }
}
