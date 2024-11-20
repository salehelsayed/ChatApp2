# Phase 1 Tests Documentation

## Domain Layer Tests

### Model Tests
- Message Tests
  - Message creation with all fields preserves values
  - Attachment creation with required fields generates valid ID
  - Message with max attachments is valid
  - Message with content at max length is valid
  - All attachment types are supported

### UseCase Tests
- Settings UseCases
  - GetThemeUseCase
  - UpdateThemeUseCase
  - GetNotificationsUseCase
  - UpdateNotificationsUseCase

## Data Layer Tests

### DTO and Mapper Tests
- SettingsDto Tests
- SettingsMapper Tests

### Data Source Tests
- SettingsDataStore Tests
- PreferencesManager Tests
  - Theme Preferences
    - test_getTheme_returnsCurrentTheme()
    - test_updateTheme_persistsNewTheme()
    - test_themeFlow_emitsUpdates()

### Repository Tests
- SettingsRepository Tests
  - Verify proper data flow between data sources
  - Test caching strategy
  - Validate error handling

## Test Implementation Status
- [x] Domain Layer Tests (Completed)
- [x] Data Layer Tests (Completed: 4/4)
  - [x] SettingsDto Tests
  - [x] SettingsMapper Tests
  - [x] SettingsDataStore Tests
  - [x] PreferencesManager Tests

## Test Execution Guide

### Unit Tests (JVM) - `app/src/test/`
These tests run on your local JVM and are faster to execute. They include:
- All Domain Layer tests
- Data Layer tests that don't require Android context (DTOs, Mappers)

To run:
1. In Android Studio: Right-click on `app/src/test` directory → Run Tests
2. Command line: `./gradlew test`

### Instrumented Tests - `app/src/androidTest/`
These tests require an Android device or emulator. They include:
- Data Layer tests that require Android context (DataStore, SharedPreferences)

To run:
1. In Android Studio: Right-click on `app/src/androidTest` directory → Run Tests
2. Command line: `./gradlew connectedAndroidTest`

**Note:** Make sure you have an Android device connected or an emulator running before executing instrumented tests.
