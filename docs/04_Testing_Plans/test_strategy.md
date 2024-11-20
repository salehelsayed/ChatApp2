# Testing Strategy

## Planned Improvements 
1. Testing
   - [x] Add unit tests for Repository
   - [x] Add integration tests for DataStore
   - [x] Add unit tests for ViewModel
     - [x] Initial state tests
     - [x] Theme update tests
     - [x] Notifications update tests
     - [x] Error handling tests
     - [x] UI state tests
   - [ ] Add UI tests for Settings screen

## Testing Strategy
- Add snapshot testing for UI components
- Implement proper test doubles for DataStore
- Add proper coroutine testing
- Consider using Turbine for Flow testing

### Phase 3: Testing Infrastructure (Week 3)

#### 3.1 Test Directory Structure
```
app/src/test/java/com/example/cheatsignal/
├── base/
│   ├── BaseTest.kt
│   └── BaseViewModelTest.kt
├── utils/
│   ├── TestDispatcherRule.kt
│   └── TestData.kt
├── viewmodels/
│   └── SettingsViewModelTest.kt
├── repository/
│   └── SettingsRepositoryTest.kt
└── ui/
    └── screens/
        └── SettingsScreenTest.kt
```

**Implementation Steps:**
1. Set up test infrastructure
2. Create base test classes
3. Add test utilities
4. Implement ViewModel tests
5. Add UI tests

## MVP Domain Layer Test Plan

### 1. Model Tests

#### 1.1 Theme Tests [x]
- [x] Test fromString() conversion
  - Valid themes (LIGHT, DARK, SYSTEM)
  - Invalid theme handling
  - Default to SYSTEM
  - Case insensitivity
  - Empty string handling

#### 1.2 Settings Tests [x]
- [x] Test default values
  - Default theme is SYSTEM
  - Default notifications enabled
  - Default message preview enabled
  - Default sound enabled
  - Default vibration enabled
  - Default timestamp
- [x] Test custom values
  - Theme preference changes
  - Notifications toggle
  - Message preview toggle
  - Sound toggle
  - Vibration toggle
  - Timestamp updates
- [x] Test data class functionality
  - Copy behavior
  - Equals implementation

#### 1.3 Message Tests [x]
- [x] Test message creation
  - ID generation
  - Required fields validation
  - Default values
- [x] Test attachments
  - Attachment creation
  - Type validation
  - Size limits
  - MAX_ATTACHMENTS limit
- [x] Test content validation
  - MAX_CONTENT_LENGTH
  - Empty content handling
- [x] Test metadata
  - Add/remove metadata
  - Empty metadata handling
- [x] Test data class functionality
  - Copy behavior
  - Equals implementation

#### 1.4 Conversation Tests [x]
- [x] Test conversation creation
  - ID generation
  - Required fields validation
  - Default values
- [x] Test participants
  - Add/remove participants
  - Role management
  - MAX_GROUP_PARTICIPANTS limit
  - MAX_BROADCAST_RECIPIENTS limit
- [x] Test conversation types
  - Direct messaging
  - Group chat
  - Broadcast messaging
- [x] Test metadata and timestamps
  - Creation timestamp
  - Update timestamp
  - Metadata operations

### 3. Test Setup [x]
- [x] Add JUnit dependencies
- [x] Add test assertion library (Google Truth)
- [x] Set up test directories

### Success Criteria
- [x] Theme tests pass
- [x] Core functionality covered (100% - 4/4 models)
- [x] Basic error handling tested (100% - 4/4 models)

## Data Layer Test Implementation Plan

### 1. SettingsDto Tests
```kotlin
class SettingsDtoTest {
    // Default Values
    test_defaultConstructor_setsCorrectDefaults()
    
    // Custom Values
    test_customValues_preservedCorrectly()
    
    // Data Integrity
    test_copyWithModifications_preservesOtherValues()
    test_toString_containsAllFields()
    test_equals_comparesAllFields()
}
```

### 2. SettingsDataStore Tests
```kotlin
@RunWith(RobolectricTestRunner::class)
class SettingsDataStoreTest {
    // Save Operations
    test_saveSettings_persistsData()
    test_saveSettings_overwritesExistingData()
    
    // Load Operations
    test_loadSettings_returnsDefaultsWhenEmpty()
    test_loadSettings_returnsStoredData()
    
    // Flow Collection
    test_settingsFlow_emitsUpdates()
    test_settingsFlow_emitsInitialValue()
    
    // Error Handling
    test_saveSettings_handlesIOException()
    test_loadSettings_handlesCorruption()
}
```

### 3. PreferencesManager Tests
```kotlin
class PreferencesManagerTest {
    // Theme Preferences
    test_getTheme_returnsCurrentTheme()
    test_updateTheme_persistsNewTheme()
    test_themeFlow_emitsUpdates()
    
    // Notification Preferences
    test_getNotifications_returnsCurrentState()
    test_updateNotifications_persistsNewState()
    test_notificationsFlow_emitsUpdates()
    
    // Error Handling
    test_updatePreferences_handlesDataStoreErrors()
    test_getPreferences_handlesMissingData()
}
```

### 4. SettingsMapper Tests
```kotlin
class SettingsMapperTest {
    // Domain to DTO
    test_toDto_withDefaultSettings()
    test_toDto_withCustomSettings()
    test_toDto_handlesNullValues()
    
    // DTO to Domain
    test_toDomain_withValidTheme()
    test_toDomain_withInvalidTheme()
    test_toDomain_preservesAllSettings()
    
    // Round Trip
    test_roundTripConversion_preservesValues()
    test_roundTripConversion_handlesInvalidTheme()
    
    // Edge Cases
    test_conversion_handlesEmptyStrings()
    test_conversion_handlesExtremeValues()
}
```

### Implementation Strategy

1. **Test Dependencies**
   ```kotlin
   // build.gradle.kts
   testImplementation("junit:junit:4.13.2")
   testImplementation("com.google.truth:truth:1.1.3")
   testImplementation("org.robolectric:robolectric:4.9")
   testImplementation("io.mockk:mockk:1.13.3")
   testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
   testImplementation("androidx.test:core:1.5.0")
   ```

2. **Test Utilities**
   - TestDispatcherRule for coroutines
   - Test data factories
   - Common test assertions

3. **Coverage Goals**
   - 90%+ line coverage
   - All edge cases covered
   - Error scenarios tested
   - Async operations verified

4. **Implementation Order**
   1. SettingsDto tests (model validation)
   2. SettingsMapper tests (data conversion)
   3. SettingsDataStore tests (persistence)
   4. PreferencesManager tests (business logic)

5. **Quality Checks**
   - Code review checklist
   - Test naming conventions
