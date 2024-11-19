package com.example.cheatsignal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cheatsignal.ui.viewmodels.SettingsViewModel
import com.example.cheatsignal.ui.viewmodels.SettingsViewModelFactory
import com.example.cheatsignal.ui.theme.CheatSignalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(LocalContext.current)
    ),
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Profile Section
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Column {
                            Text(
                                text = "John Doe",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "john.doe@example.com",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Theme Preference
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Theme",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box {
                            var expanded by remember { mutableStateOf(false) }
                            OutlinedButton(
                                onClick = { expanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(uiState.themePreference.name.lowercase().capitalize())
                                    Icon(Icons.Default.KeyboardArrowDown, "Show theme options")
                                }
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                ThemePreference.values().forEach { theme ->
                                    DropdownMenuItem(
                                        text = { Text(theme.name.lowercase().capitalize()) },
                                        onClick = {
                                            viewModel.updateTheme(theme)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Notifications
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Notifications",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Enable push notifications",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Switch(
                            checked = uiState.notificationsEnabled,
                            onCheckedChange = { enabled ->
                                viewModel.updateNotifications(enabled)
                            }
                        )
                    }
                }
            }
        }

        // Error handling
        if (uiState.error != null) {
            AlertDialog(
                onDismissRequest = { viewModel.clearError() },
                title = { Text("Error") },
                text = { Text(uiState.error!!) },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

enum class ThemePreference {
    LIGHT, DARK, SYSTEM
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    CheatSignalTheme {
        SettingsScreen(
            onNavigateBack = {}
        )
    }
}
