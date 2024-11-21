# Menu Syndical Implementation (MVP)

## Overview
This document outlines the MVP (Minimum Viable Product) implementation plan for the Syndical section of the Menu system. The Syndical screen will feature a tabbed interface with two main sections: Jobs & Skills and Hashtags.

## Implementation Requirements

### Dependencies
Add to build.gradle:
```kotlin
// Existing dependencies should be sufficient as we're using:
// - Room for database
// - Hilt for dependency injection
// - Jetpack Compose for UI
// - Material3 for design
```

## Implementation Steps

### 1. Data Layer & Models

#### Job & Skill Model
```kotlin
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

#### Hashtag Model
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

### 2. Database Migration
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

### 3. DAO Implementation
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
    
    // Hashtags
    @Query("SELECT * FROM hashtags ORDER BY usageCount DESC, lastUsed DESC")
    fun getTrendingHashtags(): Flow<List<Hashtag>>
    
    @Query("SELECT * FROM hashtags WHERE tag LIKE '%' || :query || '%'")
    fun searchHashtags(query: String): Flow<List<Hashtag>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHashtag(hashtag: Hashtag)
    
    @Query("UPDATE hashtags SET usageCount = usageCount + 1, lastUsed = :timestamp WHERE id = :hashtagId")
    suspend fun incrementHashtagUsage(hashtagId: String, timestamp: Long = System.currentTimeMillis())
}
```

### 4. Repository Layer
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
    
    // Hashtags
    fun getTrendingHashtags() = syndicalDao.getTrendingHashtags()
    fun searchHashtags(query: String) = syndicalDao.searchHashtags(query)
    
    suspend fun addHashtag(hashtag: Hashtag) {
        syndicalDao.insertHashtag(hashtag)
    }
    
    suspend fun incrementHashtagUsage(hashtagId: String) {
        syndicalDao.incrementHashtagUsage(hashtagId)
    }
}
```

### 5. Dependency Injection
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

### 6. ViewModel Implementation
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
    
    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
        loadData()
    }
    
    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchData(query)
    }
    
    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                when (_uiState.value.selectedTab) {
                    0 -> loadJobsAndSkills()
                    1 -> loadHashtags()
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message, isLoading = false)
                }
            }
        }
    }
}
```

### 7. Basic UI Components
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
                    onClick = { viewModel.onTabSelected(0) }
                ) {
                    Text("Jobs & Skills")
                }
                Tab(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.onTabSelected(1) }
                ) {
                    Text("Hashtags")
                }
            }
            
            when (uiState.selectedTab) {
                0 -> JobsSkillsTab(
                    items = uiState.jobsAndSkills,
                    onAddClick = { viewModel.showAddDialog() }
                )
                1 -> HashtagsTab(
                    hashtags = uiState.hashtags,
                    onAddClick = { viewModel.showAddDialog() }
                )
            }
        }
    }
}
```

### 8. Navigation Setup
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

### 9. Feature Implementation
- Implement detailed UI components for each tab
- Add search functionality
- Create add/edit dialogs
- Implement error handling and loading states

### 10. Testing
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
    viewModel.onSearchQueryChanged("engineer")
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
