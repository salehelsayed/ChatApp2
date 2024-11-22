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
import com.example.cheatsignal.data.model.JobSkill
import com.example.cheatsignal.data.model.SkillType
import com.example.cheatsignal.ui.screens.menu.syndical.state.JobSkillDialogState

@Composable
fun JobsSkillsTab(
    items: List<JobSkill>,
    onAddClick: () -> Unit,
    onItemClick: (JobSkill) -> Unit,
    onDeleteClick: (JobSkill) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                JobSkillCard(
                    jobSkill = item,
                    onClick = { onItemClick(item) },
                    onDeleteClick = { onDeleteClick(item) }
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
fun JobSkillCard(
    jobSkill: JobSkill,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = jobSkill.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = jobSkill.type.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = jobSkill.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, "Delete")
            }
        }
    }
}

@Composable
fun JobSkillDialog(
    state: JobSkillDialogState,
    onDismiss: () -> Unit,
    onTitleChange: (String) -> Unit,
    onTypeChange: (SkillType) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSave: (String, SkillType, String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (state.isEditing) "Edit Job/Skill" else "Add Job/Skill") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.title,
                    onValueChange = onTitleChange,
                    label = { Text("Title") }
                )
                Row {
                    RadioButton(
                        selected = state.type == SkillType.JOB,
                        onClick = { onTypeChange(SkillType.JOB) }
                    )
                    Text("Job")
                    Spacer(Modifier.width(16.dp))
                    RadioButton(
                        selected = state.type == SkillType.SKILL,
                        onClick = { onTypeChange(SkillType.SKILL) }
                    )
                    Text("Skill")
                }
                OutlinedTextField(
                    value = state.description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Description") }
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
                onClick = { onSave(state.title, state.type, state.description) }
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
