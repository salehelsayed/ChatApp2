package com.example.cheatsignal.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MessageTest {
    
    @Test
    fun `message creation with required fields generates valid ID and defaults`() {
        // When
        val message = Message(
            conversationId = "conv123",
            senderId = "sender123",
            content = "Hello, world!"
        )
        
        // Then
        with(message) {
            assertThat(id).isNotEmpty()
            assertThat(conversationId).isEqualTo("conv123")
            assertThat(senderId).isEqualTo("sender123")
            assertThat(content).isEqualTo("Hello, world!")
            assertThat(timestamp).isNotEqualTo(0L)
            assertThat(isRead).isFalse()
            assertThat(isDelivered).isFalse()
            assertThat(attachments).isEmpty()
            assertThat(metadata).isEmpty()
        }
    }
    
    @Test
    fun `message creation with all fields preserves values`() {
        // Given
        val customId = "msg123"
        val attachmentList = listOf(
            Message.Attachment(
                id = "att123",
                type = Message.AttachmentType.IMAGE,
                url = "https://example.com/image.jpg",
                size = 1024L,
                name = "image.jpg",
                mimeType = "image/jpeg"
            )
        )
        val metadataMap = mapOf("key1" to "value1", "key2" to "value2")
        val customTimestamp = 123456789L
        
        // When
        val message = Message(
            id = customId,
            conversationId = "conv123",
            senderId = "sender123",
            content = "Hello with attachments!",
            timestamp = customTimestamp,
            isRead = true,
            isDelivered = true,
            attachments = attachmentList,
            metadata = metadataMap
        )
        
        // Then
        with(message) {
            assertThat(id).isEqualTo(customId)
            assertThat(timestamp).isEqualTo(customTimestamp)
            assertThat(isRead).isTrue()
            assertThat(isDelivered).isTrue()
            assertThat(attachments).hasSize(1)
            assertThat(metadata).hasSize(2)
            assertThat(metadata["key1"]).isEqualTo("value1")
        }
    }
    
    @Test
    fun `attachment creation with required fields generates valid ID`() {
        // When
        val attachment = Message.Attachment(
            type = Message.AttachmentType.DOCUMENT,
            url = "https://example.com/doc.pdf",
            size = 2048L,
            name = "document.pdf",
            mimeType = "application/pdf"
        )
        
        // Then
        assertThat(attachment.id).isNotEmpty()
        assertThat(attachment.type).isEqualTo(Message.AttachmentType.DOCUMENT)
        assertThat(attachment.url).isEqualTo("https://example.com/doc.pdf")
        assertThat(attachment.size).isEqualTo(2048L)
        assertThat(attachment.name).isEqualTo("document.pdf")
        assertThat(attachment.mimeType).isEqualTo("application/pdf")
    }
    
    @Test
    fun `message with max attachments is valid`() {
        // Given
        val maxAttachments = List(Message.MAX_ATTACHMENTS) {
            Message.Attachment(
                type = Message.AttachmentType.IMAGE,
                url = "https://example.com/image$it.jpg",
                size = 1024L,
                name = "image$it.jpg",
                mimeType = "image/jpeg"
            )
        }
        
        // When
        val message = Message(
            conversationId = "conv123",
            senderId = "sender123",
            content = "Message with max attachments",
            attachments = maxAttachments
        )
        
        // Then
        assertThat(message.attachments).hasSize(Message.MAX_ATTACHMENTS)
    }
    
    @Test
    fun `message with content at max length is valid`() {
        // Given
        val maxContent = "a".repeat(Message.MAX_CONTENT_LENGTH)
        
        // When
        val message = Message(
            conversationId = "conv123",
            senderId = "sender123",
            content = maxContent
        )
        
        // Then
        assertThat(message.content).hasLength(Message.MAX_CONTENT_LENGTH)
    }
    
    @Test
    fun `all attachment types are supported`() {
        // When/Then
        Message.AttachmentType.values().forEach { type ->
            val attachment = Message.Attachment(
                type = type,
                url = "https://example.com/file",
                size = 1024L,
                name = "file",
                mimeType = "application/octet-stream"
            )
            assertThat(attachment.type).isEqualTo(type)
        }
    }
    
    @Test
    fun `data class equals and copy work correctly`() {
        // Given
        val message1 = Message(
            id = "msg123",
            conversationId = "conv123",
            senderId = "sender123",
            content = "Test message",
            timestamp = 123L,
            isRead = true,
            isDelivered = true,
            attachments = emptyList(),
            metadata = mapOf("key" to "value")
        )
        
        val message2 = message1.copy()
        val different = message1.copy(content = "Different content")
        
        // Then
        assertThat(message1).isEqualTo(message2)
        assertThat(message1).isNotEqualTo(different)
        assertThat(message2.metadata).isEqualTo(message1.metadata)
    }
}
