# Menu Phase 1 MVP Implementation Guide

## Overview
This guide outlines the minimal viable implementation of Phase 1 (Basic Menu Structure) of the CheatSignal menu system, focusing on essential functionality and simplicity.

## Technical Requirements

### Dependencies
Already included in build.gradle:
```kotlin
implementation 'androidx.compose.material3:material3'
implementation 'androidx.compose.ui:ui'
implementation 'androidx.navigation:navigation-compose:2.7.6'
implementation 'androidx.hilt:hilt-navigation-compose:1.1.0'
implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0'
```

## Implementation Steps

### 1. Basic Menu State and ViewModel
Location: `app/src/main/java/com/example/cheatsignal/ui/screens/menu/MenuState.kt`

```kotlin
data class MenuState(
    val selectedSection: MenuSection = MenuSection.COMMUNAL
)

enum class MenuSection {
    COMMUNAL,
    SYNDICAL,
    TRIBAL
}
```

Location: `app/src/main/java/com/example/cheatsignal/ui/viewmodels/MenuViewModel.kt`

```kotlin
@HiltViewModel
class MenuViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MenuState())
    val uiState: StateFlow<MenuState> = _uiState.asStateFlow()

    fun selectSection(section: MenuSection) {
        _uiState.update { it.copy(selectedSection = section) }
    }
}
```

### 2. Menu Screen Implementation
Location: `app/src/main/java/com/example/cheatsignal/ui/screens/menu/MenuScreen.kt`

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onNavigateBack: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu") },
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MenuSection.values().forEach { section ->
                MenuSectionCard(
                    section = section,
                    isSelected = section == uiState.selectedSection,
                    onClick = { viewModel.selectSection(section) }
                )
            }
        }
    }
}

@Composable
private fun MenuSectionCard(
    section: MenuSection,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = section.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuScreenPreview() {
    MenuScreen(onNavigateBack = {})
}
```

### 3. Navigation Integration
Add to existing navigation graph in `MainActivity.kt`:

```kotlin
composable(route = "menu") {
    MenuScreen(
        onNavigateBack = { navController.navigateUp() }
    )
}
```

## Testing
Basic preview composable included in MenuScreen.kt for visual testing.

## Success Criteria for MVP Phase 1
- [x] Menu screen displays with three sections
- [x] Sections are selectable with visual feedback
- [x] Back navigation works correctly
- [x] UI matches Material3 design system
- [x] Integration with existing navigation system
