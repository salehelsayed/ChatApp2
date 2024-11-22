package com.example.cheatsignal.ui.screens.menu.syndical.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cheatsignal.data.model.Hashtag
import com.example.cheatsignal.ui.screens.menu.syndical.state.HashtagDialogState

@Composable
fun HashtagsTab(
    hashtags: List<Hashtag>,
    onAddClick: () -> Unit,
    onItemClick: (Hashtag) -> Unit,
    onDeleteClick: (Hashtag) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(hashtags) { hashtag ->
                HashtagCard(
                    hashtag = hashtag,
                    onClick = { onItemClick(hashtag) },
                    onDeleteClick = { onDeleteClick(hashtag) }
                )
            }
        }
        
        FloatingActionButton(
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, "Add")
        }
    }
}

@Composable
fun HashtagCard(
    hashtag: Hashtag,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${hashtag.tag}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, "Delete")
            }
        }
    }
}

@Composable
fun HashtagDialog(
    state: HashtagDialogState,
    onDismiss: () -> Unit,
    onTagChange: (String) -> Unit,
    onSave: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (state.isEditing) "Edit Hashtag" else "Add Hashtag") },
        text = {
            Column {
                OutlinedTextField(
                    value = state.tag,
                    onValueChange = onTagChange,
                    label = { Text("Hashtag") }
                )
                if (state.error != null) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(state.tag) }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
