# Menu Phase 2 Communal Implementation Guide

## Overview
This guide outlines the implementation of Phase 2 (Communal Section) of the CheatSignal menu system, focusing on the XAL-based form structure and data persistence.

## Technical Requirements

### Dependencies
Add to build.gradle:
```kotlin
// XML Processing
implementation 'org.simpleframework:simple-xml:2.7.1'

// Form Validation
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.compose.material3:material3:1.1.2'

// Data Persistence
implementation 'androidx.room:room-runtime:2.6.1'
implementation 'androidx.room:room-ktx:2.6.1'
kapt 'androidx.room:room-compiler:2.6.1'
```

## Implementation Steps

### 1. XAL Schema Parser
Location: `app/src/main/java/com/example/cheatsignal/data/xal/XalParser.kt`

```kotlin
class XalParser @Inject constructor() {
    @Throws(XalParseException::class)
    fun parseSchema(schemaContent: String): XalSchema {
        // Implementation for parsing XAL.xsd schema
    }
}

data class XalSchema(
    val elements: List<XalElement>,
    val types: List<XalType>
)

data class XalElement(
    val name: String,
    val type: String,
    val minOccurs: Int = 1,
    val maxOccurs: Int = 1
)

data class XalType(
    val name: String,
    val fields: List<XalField>
)

data class XalField(
    val name: String,
    val type: XalFieldType,
    val required: Boolean = true,
    val pattern: String? = null
)

enum class XalFieldType {
    STRING,
    NUMBER,
    DATE,
    BOOLEAN
}
```

### 2. Communal Form State
Location: `app/src/main/java/com/example/cheatsignal/ui/screens/menu/communal/CommunalFormState.kt`

```kotlin
data class CommunalFormState(
    val fields: Map<String, String> = emptyMap(),
    val errors: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false
)
```

### 3. Communal Form ViewModel
Location: `app/src/main/java/com/example/cheatsignal/ui/screens/menu/communal/CommunalFormViewModel.kt`

```kotlin
@HiltViewModel
class CommunalFormViewModel @Inject constructor(
    private val xalParser: XalParser,
    private val communalRepository: CommunalRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommunalFormState())
    val uiState: StateFlow<CommunalFormState> = _uiState.asStateFlow()

    fun updateField(fieldName: String, value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                fields = currentState.fields + (fieldName to value),
                errors = currentState.errors - fieldName
            )
        }
    }

    fun validateForm(): Boolean {
        // Implementation for form validation
    }

    fun submitForm() {
        // Implementation for form submission
    }
}
```

### 4. Communal Form UI
Location: `app/src/main/java/com/example/cheatsignal/ui/screens/menu/communal/CommunalFormScreen.kt`

```kotlin
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
                title = { Text("Communal Information") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        // Form implementation with dynamic fields
    }
}

@Composable
private fun DynamicFormField(
    field: XalField,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    // Implementation for dynamic form fields based on XAL field type
}
```

### 5. Data Persistence
Location: `app/src/main/java/com/example/cheatsignal/data/local/CommunalDatabase.kt`

```kotlin
@Entity(tableName = "communal_data")
data class CommunalEntity(
    @PrimaryKey val id: String,
    val fieldName: String,
    val fieldValue: String,
    val lastModified: Long = System.currentTimeMillis()
)

@Dao
interface CommunalDao {
    @Query("SELECT * FROM communal_data")
    fun getAllFields(): Flow<List<CommunalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertField(field: CommunalEntity)

    @Query("DELETE FROM communal_data")
    suspend fun clearAll()
}
```

### 6. Repository Implementation
Location: `app/src/main/java/com/example/cheatsignal/data/repository/CommunalRepository.kt`

```kotlin
@Singleton
class CommunalRepository @Inject constructor(
    private val communalDao: CommunalDao
) {
    fun getAllFields(): Flow<List<CommunalEntity>> = communalDao.getAllFields()

    suspend fun saveField(fieldName: String, value: String) {
        communalDao.insertField(
            CommunalEntity(
                id = UUID.randomUUID().toString(),
                fieldName = fieldName,
                fieldValue = value
            )
        )
    }

    suspend fun clearAllFields() = communalDao.clearAll()
}
```

## Testing
1. Unit tests for XAL schema parsing
2. Unit tests for form validation
3. Unit tests for data persistence
4. UI tests for form interaction

## Success Criteria for Phase 2
- [ ] XAL schema successfully parsed and validated
- [ ] Dynamic form fields generated based on schema
- [ ] Form validation working correctly
- [ ] Data persistence implemented and working
- [ ] UI matches Material3 design system
- [ ] All unit tests passing
- [ ] All UI tests passing
