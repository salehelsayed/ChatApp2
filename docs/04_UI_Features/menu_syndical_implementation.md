# Menu Syndical Implementation (MVP)

## Overview
This document outlines the MVP (Minimum Viable Product) implementation plan for the Syndical section of the Menu system. The Syndical screen will feature a tabbed interface with two main sections: Jobs & Skills and Hashtags.

## Implementation Requirements

### Dependencies [Done]
Add to build.gradle:
```kotlin
// Existing dependencies should be sufficient as we're using:
// - Room for database
// - Hilt for dependency injection
// - Jetpack Compose for UI
// - Material3 for design
```

## Implementation Steps

### 1. Data Layer & Models [Done]

#### Job & Skill Model [Done]
```kotlin
package com.example.cheatsignal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "jobs_skills")
data class JobSkill(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val type: SkillType,
    val description: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis()
)

enum class SkillType {
    JOB,
    SKILL
}
```

**Key Components:** [Done]
1. **Entity Configuration** [Done]
   - Table name: "jobs_skills"
   - Primary key with UUID generation
   - Timestamp fields for tracking

2. **Data Fields** [Done]
   - `id`: Unique identifier (Primary Key, auto-generated UUID)
   - `title`: Name of the job or skill (Required)
   - `type`: SkillType enum to distinguish between JOB and SKILL
   - `description`: Detailed description of the job or skill
   - `createdAt`: Creation timestamp (auto-generated)
   - `lastModified`: Last modification timestamp (auto-generated)

3. **Type Enumeration** [Done]
   - `SkillType.JOB`: Represents a job entry
   - `SkillType.SKILL`: Represents a skill entry

4. **Room Annotations** [Done]
   - `@Entity`: Marks class as a database table
   - `@PrimaryKey`: Designates primary key field

5. **Best Practices** [Done]
   - All fields are non-nullable for data integrity
   - Default values for timestamps and ID
   - Clear separation between job and skill entries

6. **Usage Considerations** [Done]
   - Use `type` field for filtering and categorization
   - Update `lastModified` when modifying entries
   - Maintain unique titles within each type

#### Hashtag Model [Done]
```kotlin
@Entity(tableName = "hashtags")
data class Hashtag(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val tag: String,
    val usageCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUsed: Long = System.currentTimeMillis()
)
```

### 2. Database Migration [Done]
```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create jobs_skills table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS jobs_skills (
                id TEXT PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                type TEXT NOT NULL,
                description TEXT NOT NULL,
                createdAt INTEGER NOT NULL,
                lastModified INTEGER NOT NULL
            )
        """)
        
        // Create hashtags table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS hashtags (
                id TEXT PRIMARY KEY NOT NULL,
                tag TEXT NOT NULL,
                usageCount INTEGER NOT NULL,
                createdAt INTEGER NOT NULL,
                lastUsed INTEGER NOT NULL
            )
        """)
    }
}
```

### 2.1 Database Configuration [Done]
```kotlin
@Database(
    entities = [
        CommunalAddress::class,
        JobSkill::class,
        Hashtag::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun communalDao(): CommunalDao
    abstract fun syndicalDao(): SyndicalDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            // Migration implementation as shown above
        }
    }
}
```

**Key Components:** [Done]
1. **Database Configuration** [Done]
   - Added new entities: `JobSkill` and `Hashtag`
   - Updated version from 1 to 2
   - Added new DAO: `syndicalDao()`

2. **Migration Integration** [Done]
   - Migration object defined in companion object
   - Accessible via `AppDatabase.MIGRATION_1_2`
   - Applied in `DatabaseModule` using `.addMigrations()`

3. **Usage Notes** [Done]
   - Migration runs automatically when app updates from v1 to v2
   - New tables are created without affecting existing data
   - Both DAOs are accessible through dependency injection

#### Type Converters [Done]
```kotlin
class SyndicalConverters {
    @TypeConverter
    fun fromSkillType(value: SkillType): String {
        return value.name
    }

    @TypeConverter
    fun toSkillType(value: String): SkillType {
        return SkillType.valueOf(value)
    }
}
```

**Key Components:** [Done]
1. **Type Conversion** [Done]
   - Convert SkillType enum to/from String for SQLite storage
   - Use enum name as the stored value
   - Automatic conversion through Room's type converter system

2. **Usage** [Done]
   - Add `@TypeConverters(SyndicalConverters::class)` to AppDatabase
   - Room automatically handles conversion for JobSkill entity
   - Ensures type safety when working with SkillType





### 3. DAO Implementation [Done]
```kotlin
@Dao
interface SyndicalDao {
    // Jobs & Skills
    @Query("SELECT * FROM jobs_skills ORDER BY lastModified DESC")
    fun getJobsAndSkills(): Flow<List<JobSkill>>
    
    @Query("SELECT * FROM jobs_skills WHERE type = :type ORDER BY title ASC")
    fun getJobsOrSkills(type: SkillType): Flow<List<JobSkill>>
    
    @Query("SELECT * FROM jobs_skills WHERE title LIKE '%' || :query || '%'")
    fun searchJobsAndSkills(query: String): Flow<List<JobSkill>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobSkill(jobSkill: JobSkill)

    @Delete
    suspend fun deleteJobSkill(jobSkill: JobSkill)
    
    // Hashtags
    @Query("SELECT * FROM hashtags ORDER BY usageCount DESC, lastUsed DESC")
    fun getTrendingHashtags(): Flow<List<Hashtag>>
    
    @Query("SELECT * FROM hashtags WHERE tag LIKE '%' || :query || '%'")
    fun searchHashtags(query: String): Flow<List<Hashtag>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHashtag(hashtag: Hashtag)

    @Delete
    suspend fun deleteHashtag(hashtag: Hashtag)
    
    @Query("UPDATE hashtags SET usageCount = usageCount + 1, lastUsed = :timestamp WHERE id = :hashtagId")
    suspend fun incrementHashtagUsage(hashtagId: String, timestamp: Long = System.currentTimeMillis())
}

#### MVP Data Validation Requirements
1. **JobSkill Validation** [Done]
   - `title`: Required, max length 100 characters
   - `description`: Optional, max length 500 characters
   - `type`: Must be either JOB or SKILL
   - Duplicate titles allowed but not recommended for MVP

2. **Hashtag Validation** [Done]
   - `tag`: Required, max length 50 characters
   - `tag` format: Alphanumeric and underscores only
   - `usageCount`: Non-negative integer
   - Duplicate tags allowed but not recommended for MVP

#### MVP Operations
1. **Create Operations** [Done]
   - Single item insert for both JobSkill and Hashtag
   - Use `OnConflictStrategy.REPLACE` for updates
   - Auto-generate timestamps and IDs

2. **Read Operations** [Done]
   - Get all items ordered by lastModified/usageCount
   - Search by title/tag with case-insensitive LIKE
   - Filter JobSkills by type

3. **Update Operations** [Done]
   - Full item replacement via insert with REPLACE strategy
   - Increment hashtag usage count
   - Auto-update lastModified/lastUsed timestamps

4. **Delete Operations** [Done]
   - Single item deletion
   - No cascade deletion required for MVP
   - No soft delete required for MVP

5. **Error Handling** [Done]
   - Use Room's built-in SQLite constraints
   - No custom error handling required for MVP
   - Let Room handle threading via suspend functions


   

### 4. Repository Layer [Done]
```kotlin
@Singleton
class SyndicalRepository @Inject constructor(
    private val syndicalDao: SyndicalDao
) {
    // Jobs & Skills
    fun getJobsAndSkills() = syndicalDao.getJobsAndSkills()
    fun getJobsOrSkills(type: SkillType) = syndicalDao.getJobsOrSkills(type)
    fun searchJobsAndSkills(query: String) = syndicalDao.searchJobsAndSkills(query)
    
    suspend fun addJobSkill(jobSkill: JobSkill) {
        syndicalDao.insertJobSkill(jobSkill)
    }
    
    suspend fun deleteJobSkill(jobSkill: JobSkill) {
        syndicalDao.deleteJobSkill(jobSkill)
    }
    
    // Hashtags
    fun getTrendingHashtags() = syndicalDao.getTrendingHashtags()
    fun searchHashtags(query: String) = syndicalDao.searchHashtags(query)
    
    suspend fun addHashtag(hashtag: Hashtag) {
        syndicalDao.insertHashtag(hashtag)
    }
    
    suspend fun deleteHashtag(hashtag: Hashtag) {
        syndicalDao.deleteHashtag(hashtag)
    }
    
    suspend fun incrementHashtagUsage(hashtagId: String) {
        syndicalDao.incrementHashtagUsage(hashtagId)
    }
}
```

### 5. Dependency Injection [Done]
1. **Required Imports**
```kotlin
import com.example.cheatsignal.data.local.SyndicalDao
import com.example.cheatsignal.data.repository.SyndicalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
```

2. **Module Implementation**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    // Existing providers...
    
    @Provides
    fun provideSyndicalDao(appDatabase: AppDatabase): SyndicalDao {
        return appDatabase.syndicalDao()
    }
    
    @Provides
    @Singleton
    fun provideSyndicalRepository(
        syndicalDao: SyndicalDao
    ): SyndicalRepository {
        return SyndicalRepository(syndicalDao)
    }
}
```

3. **Usage in ViewModels**
```kotlin
@HiltViewModel
class SyndicalViewModel @Inject constructor(
    private val repository: SyndicalRepository
) {
    // ViewModel implementation
}
```

### 6. UI State and Events [Done]

1. **Required Imports**
```kotlin
import com.example.cheatsignal.data.model.Hashtag
import com.example.cheatsignal.data.model.JobSkill
import com.example.cheatsignal.data.model.SkillType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
```

2. **Package Declaration**
```kotlin
package com.example.cheatsignal.ui.syndical.state
```

3. **Screen State**
```kotlin
data class SyndicalScreenState(
    val selectedTab: Int = 0, // 0 for Jobs & Skills, 1 for Hashtags
    val searchQuery: String = "",
    val jobsAndSkills: List<JobSkill> = emptyList(),
    val hashtags: List<Hashtag> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showAddDialog: Boolean = false,
    val editingItem: EditingItem? = null,
    val jobSkillDialogState: JobSkillDialogState? = null,
    val hashtagDialogState: HashtagDialogState? = null
)

sealed class EditingItem {
    data class JobSkillItem(val jobSkill: JobSkill) : EditingItem()
    data class HashtagItem(val hashtag: Hashtag) : EditingItem()
}
```

4. **Dialog States**
```kotlin
data class JobSkillDialogState(
    val title: String = "",
    val type: SkillType = SkillType.JOB,
    val description: String = "",
    val error: String? = null,
    val isEditing: Boolean = false
)

data class HashtagDialogState(
    val tag: String = "",
    val error: String? = null,
    val isEditing: Boolean = false
)
```

5. **UI Events**
```kotlin
sealed class SyndicalEvent {
    // Navigation Events
    object NavigateUp : SyndicalEvent()
    
    // Tab Events
    data class TabSelected(val index: Int) : SyndicalEvent()
    
    // Search Events
    data class SearchQueryChanged(val query: String) : SyndicalEvent()
    
    // Jobs & Skills Events
    object AddJobSkillClicked : SyndicalEvent()
    data class JobSkillClicked(val jobSkill: JobSkill) : SyndicalEvent()
    data class DeleteJobSkillClicked(val jobSkill: JobSkill) : SyndicalEvent()
    data class SaveJobSkill(
        val title: String,
        val type: SkillType,
        val description: String,
        val existingId: String? = null
    ) : SyndicalEvent()
    
    // Hashtag Events
    object AddHashtagClicked : SyndicalEvent()
    data class HashtagClicked(val hashtag: Hashtag) : SyndicalEvent()
    data class DeleteHashtagClicked(val hashtag: Hashtag) : SyndicalEvent()
    data class SaveHashtag(
        val tag: String,
        val existingId: String? = null
    ) : SyndicalEvent()
    
    // Dialog Events
    object DismissDialog : SyndicalEvent()
    data class JobSkillTitleChanged(val title: String) : SyndicalEvent()
    data class JobSkillTypeChanged(val type: SkillType) : SyndicalEvent()
    data class JobSkillDescriptionChanged(val description: String) : SyndicalEvent()
    data class HashtagTextChanged(val tag: String) : SyndicalEvent()
    
    // Error Events
    object DismissError : SyndicalEvent()
}
```

6. **Usage Example**
```kotlin
@HiltViewModel
class SyndicalViewModel @Inject constructor(
    private val repository: SyndicalRepository
) {
    private val _uiState = MutableStateFlow(SyndicalScreenState())
    val uiState: StateFlow<SyndicalScreenState> = _uiState.asStateFlow()
    
    fun onEvent(event: SyndicalEvent) {
        when (event) {
            is SyndicalEvent.TabSelected -> updateTab(event.index)
            is SyndicalEvent.SearchQueryChanged -> updateSearch(event.query)
            // ... handle other events
        }
    }
    
    private fun updateTab(index: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(
                selectedTab = index,
                searchQuery = "",
                jobSkillDialogState = null,
                hashtagDialogState = null,
                showAddDialog = false
            )}
            loadData()
        }
    }
    
    private fun showAddJobSkillDialog() {
        _uiState.update { it.copy(
            showAddDialog = true,
            jobSkillDialogState = JobSkillDialogState()
        )}
    }
}
```

### 7. UI Components

1. **Required Imports**
```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
```

2. **Screen Implementation**
```kotlin
@Composable
fun SyndicalScreen(
    viewModel: SyndicalViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Syndical") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Search Bar
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.onEvent(SyndicalEvent.SearchQueryChanged(it)) }
            )
            
            // Tabs
            TabRow(selectedTabIndex = uiState.selectedTab) {
                Tab(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.onEvent(SyndicalEvent.TabSelected(0)) }
                ) {
                    Text("Jobs & Skills")
                }
                Tab(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.onEvent(SyndicalEvent.TabSelected(1)) }
                ) {
                    Text("Hashtags")
                }
            }
            
            // Content
            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.error != null -> ErrorMessage(
                    message = uiState.error,
                    onDismiss = { viewModel.onEvent(SyndicalEvent.DismissError) }
                )
                else -> when (uiState.selectedTab) {
                    0 -> JobsSkillsTab(
                        items = uiState.jobsAndSkills,
                        onAddClick = { viewModel.onEvent(SyndicalEvent.AddJobSkillClicked) },
                        onItemClick = { viewModel.onEvent(SyndicalEvent.JobSkillClicked(it)) },
                        onDeleteClick = { viewModel.onEvent(SyndicalEvent.DeleteJobSkillClicked(it)) }
                    )
                    1 -> HashtagsTab(
                        hashtags = uiState.hashtags,
                        onAddClick = { viewModel.onEvent(SyndicalEvent.AddHashtagClicked) },
                        onItemClick = { viewModel.onEvent(SyndicalEvent.HashtagClicked(it)) },
                        onDeleteClick = { viewModel.onEvent(SyndicalEvent.DeleteHashtagClicked(it)) }
                    )
                }
            }
        }
    }
    
    // Dialogs
    if (uiState.jobSkillDialogState != null) {
        JobSkillDialog(
            state = uiState.jobSkillDialogState,
            onDismiss = { viewModel.onEvent(SyndicalEvent.DismissDialog) },
            onSave = { title, type, description ->
                viewModel.onEvent(SyndicalEvent.SaveJobSkill(
                    title = title,
                    type = type,
                    description = description,
                    existingId = (uiState.editingItem as? EditingItem.JobSkillItem)?.jobSkill?.id
                ))
            }
        )
    }
    
    if (uiState.hashtagDialogState != null) {
        HashtagDialog(
            state = uiState.hashtagDialogState,
            onDismiss = { viewModel.onEvent(SyndicalEvent.DismissDialog) },
            onSave = { tag ->
                viewModel.onEvent(SyndicalEvent.SaveHashtag(
                    tag = tag,
                    existingId = (uiState.editingItem as? EditingItem.HashtagItem)?.hashtag?.id
                ))
            }
        )
    }
}

3. **Component Implementations**
```kotlin
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search...") },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true
    )
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(
    message: String?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (message != null) {
        Snackbar(
            modifier = modifier.padding(16.dp),
            action = {
                TextButton(onClick = onDismiss) {
                    Text("Dismiss")
                }
            }
        ) {
            Text(message)
        }
    }
}

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
fun JobSkillDialog(
    state: JobSkillDialogState,
    onDismiss: () -> Unit,
    onSave: (String, SkillType, String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (state.isEditing) "Edit Job/Skill" else "Add Job/Skill") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.title,
                    onValueChange = { viewModel.onEvent(SyndicalEvent.JobSkillTitleChanged(it)) },
                    label = { Text("Title") }
                )
                Row {
                    RadioButton(
                        selected = state.type == SkillType.JOB,
                        onClick = { viewModel.onEvent(SyndicalEvent.JobSkillTypeChanged(SkillType.JOB)) }
                    )
                    Text("Job")
                    Spacer(Modifier.width(16.dp))
                    RadioButton(
                        selected = state.type == SkillType.SKILL,
                        onClick = { viewModel.onEvent(SyndicalEvent.JobSkillTypeChanged(SkillType.SKILL)) }
                    )
                    Text("Skill")
                }
                OutlinedTextField(
                    value = state.description,
                    onValueChange = { viewModel.onEvent(SyndicalEvent.JobSkillDescriptionChanged(it)) },
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

@Composable
fun HashtagDialog(
    state: HashtagDialogState,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (state.isEditing) "Edit Hashtag" else "Add Hashtag") },
        text = {
            Column {
                OutlinedTextField(
                    value = state.tag,
                    onValueChange = { viewModel.onEvent(SyndicalEvent.HashtagTextChanged(it)) },
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
```

### 8. ViewModel Implementation
```kotlin
@HiltViewModel
class SyndicalViewModel @Inject constructor(
    private val repository: SyndicalRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SyndicalScreenState())
    val uiState: StateFlow<SyndicalScreenState> = _uiState.asStateFlow()
    
    init {
        loadInitialData()
    }
    
    fun onEvent(event: SyndicalEvent) {
        when (event) {
            is SyndicalEvent.TabSelected -> updateTab(event.index)
            is SyndicalEvent.SearchQueryChanged -> updateSearch(event.query)
            // ... handle other events
        }
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val jobsAndSkills = repository.getJobsAndSkills().first()
                val hashtags = repository.getTrendingHashtags().first()
                _uiState.update { it.copy(jobsAndSkills = jobsAndSkills, hashtags = hashtags, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}
```

### 9. Basic UI Components
```kotlin
@Composable
fun SyndicalScreen(
    viewModel: SyndicalViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Syndical") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = uiState.selectedTab) {
                Tab(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.onEvent(SyndicalEvent.TabSelected(0)) }
                ) {
                    Text("Jobs & Skills")
                }
                Tab(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.onEvent(SyndicalEvent.TabSelected(1)) }
                ) {
                    Text("Hashtags")
                }
            }
            
            when (uiState.selectedTab) {
                0 -> JobsSkillsTab(
                    items = uiState.jobsAndSkills,
                    onAddClick = { viewModel.onEvent(SyndicalEvent.AddJobSkillClicked) }
                )
                1 -> HashtagsTab(
                    hashtags = uiState.hashtags,
                    onAddClick = { viewModel.onEvent(SyndicalEvent.AddHashtagClicked) }
                )
            }
        }
    }
}
```

### 10. Navigation Setup
```kotlin
// In MainActivity.kt
sealed class Screen {
    // Existing screens...
    object Syndical : Screen()
}

// In MenuSection.kt
enum class MenuSection {
    // Existing sections...
    SYNDICAL
}

// In MenuViewModel.kt
fun onSyndicalClick() {
    navigate(Screen.Syndical)
}
```

### 11. Feature Implementation
- Implement detailed UI components for each tab
- Add search functionality
- Create add/edit dialogs
- Implement error handling and loading states

### 12. Testing
```kotlin
@Test
fun syndicalDao_insertAndRetrieveJobSkill() = runTest {
    val jobSkill = JobSkill(
        title = "Software Engineer",
        type = SkillType.JOB,
        description = "Develops software applications"
    )
    syndicalDao.insertJobSkill(jobSkill)
    
    val result = syndicalDao.getJobsAndSkills().first()
    assertThat(result).contains(jobSkill)
}

@Test
fun syndicalViewModel_searchJobsAndSkills() = runTest {
    viewModel.onEvent(SyndicalEvent.SearchQueryChanged("engineer"))
    val result = viewModel.uiState.value.jobsAndSkills
    assertThat(result.map { it.title })
        .contains("Software Engineer")
}
```

## Success Criteria
- [ ] Database migration successful
- [ ] CRUD operations working for both Jobs/Skills and Hashtags
- [ ] UI renders correctly with proper tab navigation
- [ ] Search functionality working in both tabs
- [ ] Add/Edit dialogs functional
- [ ] All tests passing
- [ ] Error handling implemented
- [ ] Loading states showing correctly

## Project Structure
```
app/src/main/java/com/example/cheatsignal/
├── data/
│   ├── model/
│   │   ├── JobSkill.kt
│   │   └── Hashtag.kt
│   ├── local/
│   │   ├── SyndicalDao.kt
│   │   └── AppDatabase.kt
│   └── repository/
│       └── SyndicalRepository.kt
├── di/
│   └── DatabaseModule.kt
└── ui/
    ├── screens/
    │   └── menu/
    │       └── syndical/
    │           ├── SyndicalScreen.kt
    │           ├── JobsSkillsTab.kt
    │           └── HashtagsTab.kt
    └── viewmodels/
        └── SyndicalViewModel.kt
