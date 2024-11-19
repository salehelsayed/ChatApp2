package com.example.cheatsignal.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ConversationTest {
    
    private val sampleMessage = Message(
        conversationId = "conv123",
        senderId = "sender123",
        content = "Hello!"
    )
    
    private val sampleParticipant = Conversation.Participant(
        id = "user123",
        name = "John Doe"
    )
    
    @Test
    fun `conversation creation with required fields generates valid ID and defaults`() {
        // When
        val conversation = Conversation(
            participants = listOf(sampleParticipant)
        )
        
        // Then
        with(conversation) {
            assertThat(id).isNotEmpty()
            assertThat(participants).hasSize(1)
            assertThat(lastMessage).isNull()
            assertThat(unreadCount).isEqualTo(0)
            assertThat(createdAt).isNotEqualTo(0L)
            assertThat(updatedAt).isNotEqualTo(0L)
            assertThat(type).isEqualTo(Conversation.ConversationType.DIRECT)
            assertThat(metadata).isEmpty()
        }
    }
    
    @Test
    fun `conversation creation with all fields preserves values`() {
        // Given
        val customId = "conv123"
        val participants = listOf(
            sampleParticipant,
            Conversation.Participant(
                id = "user456",
                name = "Jane Smith",
                profilePictureUrl = "https://example.com/profile.jpg",
                role = Conversation.Role.ADMIN,
                lastSeen = 123456789L,
                isOnline = true
            )
        )
        val metadataMap = mapOf("key1" to "value1", "key2" to "value2")
        val timestamp = 123456789L
        
        // When
        val conversation = Conversation(
            id = customId,
            participants = participants,
            lastMessage = sampleMessage,
            unreadCount = 5,
            createdAt = timestamp,
            updatedAt = timestamp,
            type = Conversation.ConversationType.GROUP,
            metadata = metadataMap
        )
        
        // Then
        with(conversation) {
            assertThat(id).isEqualTo(customId)
            assertThat(participants).hasSize(2)
            assertThat(lastMessage).isEqualTo(sampleMessage)
            assertThat(unreadCount).isEqualTo(5)
            assertThat(createdAt).isEqualTo(timestamp)
            assertThat(updatedAt).isEqualTo(timestamp)
            assertThat(type).isEqualTo(Conversation.ConversationType.GROUP)
            assertThat(metadata).hasSize(2)
            assertThat(metadata["key1"]).isEqualTo("value1")
        }
    }
    
    @Test
    fun `participant creation with required fields sets defaults`() {
        // When
        val participant = Conversation.Participant(
            id = "user123",
            name = "John Doe"
        )
        
        // Then
        with(participant) {
            assertThat(id).isEqualTo("user123")
            assertThat(name).isEqualTo("John Doe")
            assertThat(profilePictureUrl).isNull()
            assertThat(role).isEqualTo(Conversation.Role.MEMBER)
            assertThat(lastSeen).isNotEqualTo(0L)
            assertThat(isOnline).isFalse()
        }
    }
    
    @Test
    fun `group conversation respects maximum participants limit`() {
        // Given
        val maxParticipants = List(Conversation.MAX_GROUP_PARTICIPANTS) {
            Conversation.Participant(
                id = "user$it",
                name = "User $it"
            )
        }
        
        // When
        val conversation = Conversation(
            participants = maxParticipants,
            type = Conversation.ConversationType.GROUP
        )
        
        // Then
        assertThat(conversation.participants).hasSize(Conversation.MAX_GROUP_PARTICIPANTS)
    }
    
    @Test
    fun `broadcast conversation respects maximum recipients limit`() {
        // Given
        val maxRecipients = List(Conversation.MAX_BROADCAST_RECIPIENTS) {
            Conversation.Participant(
                id = "user$it",
                name = "User $it"
            )
        }
        
        // When
        val conversation = Conversation(
            participants = maxRecipients,
            type = Conversation.ConversationType.BROADCAST
        )
        
        // Then
        assertThat(conversation.participants).hasSize(Conversation.MAX_BROADCAST_RECIPIENTS)
    }
    
    @Test
    fun `all conversation types are supported`() {
        // When/Then
        Conversation.ConversationType.values().forEach { type ->
            val conversation = Conversation(
                participants = listOf(sampleParticipant),
                type = type
            )
            assertThat(conversation.type).isEqualTo(type)
        }
    }
    
    @Test
    fun `all participant roles are supported`() {
        // When/Then
        Conversation.Role.values().forEach { role ->
            val participant = Conversation.Participant(
                id = "user123",
                name = "John Doe",
                role = role
            )
            assertThat(participant.role).isEqualTo(role)
        }
    }
    
    @Test
    fun `data class equals and copy work correctly for conversation`() {
        // Given
        val conversation1 = Conversation(
            id = "conv123",
            participants = listOf(sampleParticipant),
            lastMessage = sampleMessage,
            unreadCount = 1,
            createdAt = 123L,
            updatedAt = 123L,
            type = Conversation.ConversationType.DIRECT,
            metadata = mapOf("key" to "value")
        )
        
        val conversation2 = conversation1.copy()
        val different = conversation1.copy(unreadCount = 2)
        
        // Then
        assertThat(conversation1).isEqualTo(conversation2)
        assertThat(conversation1).isNotEqualTo(different)
        assertThat(conversation2.metadata).isEqualTo(conversation1.metadata)
    }
    
    @Test
    fun `data class equals and copy work correctly for participant`() {
        // Given
        val participant1 = Conversation.Participant(
            id = "user123",
            name = "John Doe",
            profilePictureUrl = "https://example.com/profile.jpg",
            role = Conversation.Role.ADMIN,
            lastSeen = 123L,
            isOnline = true
        )
        
        val participant2 = participant1.copy()
        val different = participant1.copy(name = "Jane Doe")
        
        // Then
        assertThat(participant1).isEqualTo(participant2)
        assertThat(participant1).isNotEqualTo(different)
    }
}
