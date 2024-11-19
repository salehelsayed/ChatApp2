package com.example.cheatsignal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cheatsignal.model.Message
import com.example.cheatsignal.model.MessageStatus
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessageBubble(
    message: Message,
    modifier: Modifier = Modifier
) {
    val bubbleColor = if (message.isOutgoing) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val alignment = if (message.isOutgoing) {
        Alignment.CenterEnd
    } else {
        Alignment.CenterStart
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (message.isOutgoing) 64.dp else 8.dp,
                end = if (message.isOutgoing) 8.dp else 64.dp,
                top = 4.dp,
                bottom = 4.dp
            ),
        contentAlignment = alignment
    ) {
        Column {
            // Reply preview if exists
            message.replyTo?.let { replyMessage ->
                Surface(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = replyMessage.content,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            // Main message bubble
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isOutgoing) 16.dp else 4.dp,
                    bottomEnd = if (message.isOutgoing) 4.dp else 16.dp
                ),
                color = bubbleColor
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                ) {
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        modifier = Modifier.align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = formatTimestamp(message.timestamp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = if (message.isOutgoing) 4.dp else 0.dp)
                        )

                        if (message.isOutgoing) {
                            when (message.status) {
                                MessageStatus.SENT -> Icon(
                                    Icons.Filled.Check,
                                    contentDescription = "Sent",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                MessageStatus.DELIVERED, MessageStatus.READ -> Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = "Delivered",
                                    modifier = Modifier.size(16.dp),
                                    tint = if (message.status == MessageStatus.READ)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
