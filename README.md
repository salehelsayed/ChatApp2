# CheatSignal Chat App

A modern Android chat application built with Jetpack Compose and Material Design 3, inspired by Signal's clean and minimalist UI.

## 🚀 Project Overview

### Tech Stack
- **Platform**: Android
- **Language**: Kotlin 1.9.0
- **Minimum SDK**: 24
- **Target SDK**: 34
- **UI Framework**: Jetpack Compose
- **Design System**: Material Design 3
- **Architecture**: MVVM with Clean Architecture
- **State Management**: StateFlow, ViewModel
- **Data Persistence**: DataStore
- **Dependency Injection**: Custom DI with SettingsModule
- **Testing**: Coming soon

### Key Dependencies
```kotlin
// Core Dependencies
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
implementation("androidx.activity:activity-compose:1.8.2")

// Compose Dependencies
implementation(platform("androidx.compose:compose-bom:2023.10.01"))
implementation("androidx.compose.material3:material3")
implementation("androidx.navigation:navigation-compose:2.7.6")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("androidx.compose.material:material-icons-core")
implementation("androidx.compose.material:material-icons-extended")

// Data Management
implementation("androidx.datastore:datastore-preferences:1.0.0")
implementation("androidx.datastore:datastore-preferences-core:1.0.0")
```

## 🎯 Features

### Implemented (v0.2.0)
- Modern UI built with Jetpack Compose
- Material Design 3 theming
- Conversation list screen with:
  - User avatars
  - Last message preview
  - Timestamp
  - Unread message counter
- Chat detail screen with:
  - Message bubbles
  - Message status indicators
  - Responsive input field
  - Keyboard handling
  - Attachment and emoji buttons
- Settings screen with:
  - Theme selection (Light/Dark/System)
  - Notifications toggle
  - Material3 Cards UI
  - Error handling
  - Settings persistence
- Smooth navigation between screens
- Edge-to-edge design
- DataStore preferences

### In Progress (v0.3.0)
- Comprehensive testing
  - Unit tests for ViewModels
  - Unit tests for Repositories
  - UI tests for screens
  - Integration tests
- Error handling improvements
  - Retry mechanisms
  - Offline support
  - Error analytics
- Performance optimization
  - Caching layer
  - Batch updates
  - Background processing

### Planned (v0.4.0)
- Enhanced animations
- Improved accessibility
- Haptic feedback
- Rich notifications
- Profile management
- Advanced settings
- Data backup
- Theme customization

## 🏗 Architecture

### UI Layer
- **Screens**: Composable UI components with Material3
  - ConversationListScreen
  - ChatDetailScreen
  - SettingsScreen
- **ViewModels**: State management and business logic
  - SettingsViewModel with StateFlow
- **State**: Immutable state objects
  - UI state classes
  - Error handling with sealed classes

### Domain Layer
- **Models**
  - Theme enum
  - Conversation data class
  - Settings models
- **Repository Interfaces**
  - SettingsRepository

### Data Layer
- **Repositories**
  - SettingsRepositoryImpl
- **Data Sources**
  - DataStore preferences
- **Error Handling**
  - Sealed error classes
  - Error recovery

## 🎨 Design System

### Material Design 3 Implementation
- Custom color scheme
- Typography system
- Component theming
  - Cards for settings
  - TopAppBar with navigation
  - Dropdowns and switches
- Dark mode support
- Consistent spacing

### Accessibility
- Content descriptions
- Touch target sizing
- Screen reader support
- High contrast text

## 🧪 Testing Strategy

### Unit Tests (Planned)
- ViewModel tests
- Repository tests
- Use case tests
- Error handling tests

### UI Tests (Planned)
- Screen navigation tests
- Component interaction tests
- Theme switching tests
- Error state tests

### Integration Tests (Planned)
- DataStore integration
- Navigation flow
- State management
- Error recovery

## 🔒 Security

### Data Protection
- Secure preference storage
- Error handling privacy
- Input validation
- Future encryption support

## 📚 Documentation

### Technical Documentation
- Architecture overview
- Component documentation
- Error handling guide
- Testing guide

### User Documentation
- Feature guides
- Settings documentation
- Troubleshooting guide
- FAQ section

## 🚀 Getting Started

### Prerequisites
- Android Studio Latest
- JDK 11
- Android SDK 34

### Setup
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run the app

## 🤝 Contributing
Contributions are welcome! Please read our contributing guidelines.

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments
- Material Design 3 for the design system
- Jetpack Compose for the modern UI toolkit
- Android team for excellent documentation
