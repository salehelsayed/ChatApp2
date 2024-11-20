# CheatSignal Chat App

A modern Android chat application built with Jetpack Compose and Material Design 3, featuring AI-powered conversations powered by OpenAI's GPT-4.

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
- **Dependency Injection**: Hilt
- **AI Integration**: OpenAI GPT-4
- **Network**: Ktor CIO Client
- **Testing**: JUnit, Mockito

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

// Dependency Injection
implementation("com.google.dagger:hilt-android:2.48")
kapt("com.google.dagger:hilt-android-compiler:2.48")
implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

// OpenAI Integration
implementation("com.aallam.openai:openai-client:3.6.2")
implementation("io.ktor:ktor-client-cio:2.3.7")

// Data Management
implementation("androidx.datastore:datastore-preferences:1.0.0")
implementation("androidx.datastore:datastore-preferences-core:1.0.0")
```

## 🎯 Features

### Implemented (v0.3.0)
- Modern UI built with Jetpack Compose
- Material Design 3 theming
- AI-Powered Chat
  - Integration with OpenAI GPT-4
  - Custom AI personality
  - Real-time message streaming
  - Error handling and retry mechanisms
- Conversation Management
  - Dynamic conversation list
  - Unread message tracking
  - Real-time updates
  - Message persistence
- Chat Features
  - Message bubbles with status indicators
  - Loading states and error handling
  - Keyboard handling
  - Edge-to-edge design
- Settings Management
  - Theme selection (Light/Dark/System)
  - Notifications toggle
  - Settings persistence
- Error Handling
  - Network error recovery
  - API error handling
  - User-friendly error messages
- State Management
  - MVVM architecture
  - Unidirectional data flow
  - StateFlow for reactive updates

### In Progress (v0.4.0)
- Enhanced AI Features
  - Voice input/output
  - Image generation
  - Context-aware responses
- Message Features
  - Media attachments
  - Voice messages
  - Message reactions
  - Message search
- UI Improvements
  - Custom animations
  - Haptic feedback
  - Rich notifications
  - Profile customization

### Planned (v0.5.0)
- Group Chat Support
- End-to-End Encryption
- Cloud Backup
- Message Translation
- Custom Themes
- Offline Support
- Cross-Device Sync

## 🏗 Architecture

### UI Layer
- **Screens**
  - ConversationListScreen: Displays all chats with real-time updates
  - ChatDetailScreen: Handles message display and AI interactions
  - SettingsScreen: User preferences and app configuration
- **ViewModels**
  - ChatViewModel: Manages chat state and AI interactions
  - ConversationListViewModel: Handles conversation list state
  - SettingsViewModel: Manages app settings

### Domain Layer
- **Models**
  - Conversation: Represents chat threads
  - Message: Chat message with metadata
  - Settings: App configuration
- **Repositories**
  - ConversationRepository: Manages chat data
  - SettingsRepository: Handles user preferences
- **Services**
  - OpenAIService: Handles AI message generation

### Data Layer
- **Local Storage**
  - DataStore: Persists user preferences
  - Room DB (planned): Message and conversation storage
- **Network**
  - Ktor CIO Client: Efficient API communication
  - OpenAI Kotlin Client: Type-safe API integration

## 🛠 Setup

1. Clone the repository
2. Create a `config.properties` file in `app/src/main/assets/` with your OpenAI API key:
```properties
OPENAI_API_KEY=your-api-key-here
```
3. Build and run the project

## 📝 Contributing

Feel free to submit issues and enhancement requests!

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
