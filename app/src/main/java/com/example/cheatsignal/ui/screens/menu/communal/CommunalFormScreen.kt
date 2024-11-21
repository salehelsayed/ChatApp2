package com.example.cheatsignal.ui.screens.menu.communal

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunalFormScreen(
    onNavigateBack: () -> Unit,
    viewModel: CommunalFormViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Communal Address") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.addressLine,
                onValueChange = { viewModel.updateField("addressLine", it) },
                label = { Text("Address") },
                isError = "addressLine" in uiState.errors,
                supportingText = { Text(uiState.errors["addressLine"] ?: "") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.locality,
                onValueChange = { viewModel.updateField("locality", it) },
                label = { Text("City/Town") },
                isError = "locality" in uiState.errors,
                supportingText = { Text(uiState.errors["locality"] ?: "") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.postalCode,
                onValueChange = { viewModel.updateField("postalCode", it) },
                label = { Text("Postal Code") },
                isError = "postalCode" in uiState.errors,
                supportingText = { Text(uiState.errors["postalCode"] ?: "") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.country,
                onValueChange = { viewModel.updateField("country", it) },
                label = { Text("Country") },
                isError = "country" in uiState.errors,
                supportingText = { Text(uiState.errors["country"] ?: "") },
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Button(
                    onClick = { viewModel.saveAddress() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save Address")
                }
            }

            if (uiState.isSaved) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Address saved successfully!",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
