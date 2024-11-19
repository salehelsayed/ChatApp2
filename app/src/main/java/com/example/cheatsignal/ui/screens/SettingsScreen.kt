package com.example.cheatsignal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cheatsignal.ui.viewmodels.SettingsViewModel
import com.example.cheatsignal.model.Theme
import com.example.cheatsignal.ui.viewmodels.SettingsError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Theme Selection
                                Text(
                                    text = "Appearance",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                ExposedDropdownMenuBox(
                                    expanded = uiState.isThemeMenuExpanded,
                                    onExpandedChange = { viewModel.onThemeMenuExpandedChange(it) }
                                ) {
                                    OutlinedTextField(
                                        value = uiState.currentTheme.name,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Theme") },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isThemeMenuExpanded) },
                                        modifier = Modifier
                                            .menuAnchor()
                                            .fillMaxWidth()
                                    )
                                    ExposedDropdownMenu(
                                        expanded = uiState.isThemeMenuExpanded,
                                        onDismissRequest = { viewModel.onThemeMenuExpandedChange(false) }
                                    ) {
                                        Theme.values().forEach { theme ->
                                            DropdownMenuItem(
                                                text = { Text(theme.name) },
                                                onClick = {
                                                    viewModel.updateTheme(theme)
                                                    viewModel.onThemeMenuExpandedChange(false)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "Notifications",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Enable notifications",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Switch(
                                        checked = uiState.notificationsEnabled,
                                        onCheckedChange = { viewModel.updateNotifications(it) }
                                    )
                                }
                            }
                        }
                    }

                    // Error Snackbar
                    if (uiState.error != null) {
                        val errorMessage = when (val error = uiState.error) {
                            is SettingsError.StorageError -> error.message
                            is SettingsError.NetworkError -> error.message
                            is SettingsError.ValidationError -> error.message
                            null -> ""
                        }
                        Snackbar(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            action = {
                                TextButton(onClick = { viewModel.clearError() }) {
                                    Text("Dismiss")
                                }
                            }
                        ) {
                            Text(errorMessage)
                        }
                    }
                }
            }
        }
    }
}
