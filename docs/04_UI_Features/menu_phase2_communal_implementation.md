# Menu Phase 2 Communal Implementation Guide

## Overview
This guide outlines the minimal viable implementation of Phase 2 (Communal Section) of the CheatSignal menu system, focusing on essential address information and data persistence.

## Technical Requirements

### Dependencies
Add to build.gradle:
```kotlin
// Data Persistence
implementation 'androidx.room:room-runtime:2.6.1'
implementation 'androidx.room:room-ktx:2.6.1'
kapt 'androidx.room:room-compiler:2.6.1'
```

## Implementation Steps

### 1. Communal Data Model
Location: `app/src/main/java/com/example/cheatsignal/data/model/CommunalAddress.kt`

```kotlin
@Entity(tableName = "communal_addresses")
data class CommunalAddress(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val addressLine: String,
    val locality: String,
    val postalCode: String,
    val country: String,
    val lastModified: Long = System.currentTimeMillis()
)
```

### 2. Communal Form State
Location: `app/src/main/java/com/example/cheatsignal/ui/screens/menu/communal/CommunalFormState.kt`

```kotlin
data class CommunalFormState(
    val addressLine: String = "",
    val locality: String = "",
    val postalCode: String = "",
    val country: String = "",
    val errors: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)
```

### 3. Communal Form ViewModel
Location: `app/src/main/java/com/example/cheatsignal/ui/screens/menu/communal/CommunalFormViewModel.kt`

```kotlin
@HiltViewModel
class CommunalFormViewModel @Inject constructor(
    private val communalRepository: CommunalRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommunalFormState())
    val uiState: StateFlow<CommunalFormState> = _uiState.asStateFlow()

    fun updateField(field: String, value: String) {
        _uiState.update { currentState ->
            when (field) {
                "addressLine" -> currentState.copy(addressLine = value)
                "locality" -> currentState.copy(locality = value)
                "postalCode" -> currentState.copy(postalCode = value)
                "country" -> currentState.copy(country = value)
                else -> currentState
            }.copy(errors = currentState.errors - field)
        }
    }

    fun validateForm(): Boolean {
        val errors = mutableMapOf<String, String>()
        with(uiState.value) {
            if (addressLine.isBlank()) errors["addressLine"] = "Address is required"
            if (locality.isBlank()) errors["locality"] = "City/Town is required"
            if (postalCode.isBlank()) errors["postalCode"] = "Postal code is required"
            if (country.isBlank()) errors["country"] = "Country is required"
        }
        _uiState.update { it.copy(errors = errors) }
        return errors.isEmpty()
    }

    fun saveAddress() {
        if (!validateForm()) return
        viewModelScope.launch {
            with(uiState.value) {
                communalRepository.saveAddress(
                    CommunalAddress(
                        addressLine = addressLine,
                        locality = locality,
                        postalCode = postalCode,
                        country = country
                    )
                )
                _uiState.update { it.copy(isSaved = true) }
            }
        }
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
            Button(
                onClick = { viewModel.saveAddress() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save Address")
            }
        }
    }
}
```

### 5. Data Persistence
Location: `app/src/main/java/com/example/cheatsignal/data/local/CommunalDatabase.kt`

```kotlin
@Dao
interface CommunalDao {
    @Query("SELECT * FROM communal_addresses ORDER BY lastModified DESC")
    fun getAddresses(): Flow<List<CommunalAddress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: CommunalAddress)

    @Query("DELETE FROM communal_addresses")
    suspend fun clearAll()
}

@Database(entities = [CommunalAddress::class], version = 1)
abstract class CommunalDatabase : RoomDatabase() {
    abstract fun communalDao(): CommunalDao
}
```

### 6. Repository Implementation
Location: `app/src/main/java/com/example/cheatsignal/data/repository/CommunalRepository.kt`

```kotlin
@Singleton
class CommunalRepository @Inject constructor(
    private val communalDao: CommunalDao
) {
    fun getAddresses(): Flow<List<CommunalAddress>> = communalDao.getAddresses()

    suspend fun saveAddress(address: CommunalAddress) {
        communalDao.insertAddress(address)
    }

    suspend fun clearAllAddresses() = communalDao.clearAll()
}
```

## Success Criteria for Phase 2
- [ ] Basic address form implemented with essential fields
- [ ] Form validation working correctly
- [ ] Data persistence implemented and working
- [ ] UI matches Material3 design system

## Testing Phase 
1. Unit tests for form validation
2. Unit tests for data persistence
3. UI tests for form interaction

## Success Criteria for Testing Phase
- [ ] All unit tests passing
- [ ] All UI tests passing