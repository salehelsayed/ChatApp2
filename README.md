# CheatSignal Chat App (Shout)

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
- **Testing**: JUnit, Mockito, TestDispatcher

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
implementation("io.ktor:ktor-client-core:2.3.7")
implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")

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
  - Proper content negotiation and JSON serialization
- Conversation Management
  - Dynamic conversation list
  - Unread message tracking
  - Real-time updates
  - Message persistence
  - Proper conversation state handling
- Chat Features
  - Message bubbles with status indicators
  - Loading states and error handling
  - Keyboard handling with auto-capitalization
  - Edge-to-edge design
- Settings Management
  - Theme selection (Light/Dark/System)
  - Notifications toggle
  - Settings persistence
  - Comprehensive settings tests
- Error Handling
  - Network error recovery
  - API error handling
  - User-friendly error messages
  - Proper HTTP client configuration
- State Management
  - MVVM architecture
  - Unidirectional data flow
  - StateFlow for reactive updates
  - Comprehensive test coverage
- Testing
  - Unit tests for repositories
  - ViewModel tests
  - Settings persistence tests
  - Domain model tests
  - TestDispatcher for coroutines

### In Progress (v0.4.0)
- Group Chat Features
  - Group chat creation and management
  - Multi-user conversations
  - Group chat UI components
  - Real-time group updates
  - Group message synchronization
- Enhanced AI Features
  - Voice input/output
  - Image generation
  - Context-aware responses
  - Multi-turn conversations
  - Personality customization

### Planned Features (v0.5.0)
- Media Sharing
  - Image sharing
  - File attachments
  - Media preview
  - Upload progress tracking
- Advanced Group Features
  - Group roles and permissions
  - Member management
  - Group settings
  - Invite system
- Enhanced Security
  - End-to-end encryption
  - Secure file storage
  - Authentication improvements
  - Privacy settings
- Performance Optimizations
  - Message caching
  - Lazy loading
  - Background sync
  - Offline support

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

## 🛠️ Setup

### Prerequisites
- Android Studio Hedgehog | 2023.1.1
- JDK 11 or higher
- Android SDK 34
- OpenAI API Key

### Configuration
1. Clone the repository
2. Create `app/src/main/assets/config.properties`
3. Add your OpenAI API key:
   ```properties
   OPENAI_API_KEY=your_api_key_here
   ```
4. Build and run the project

### Building
```bash
# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Build release APK
./gradlew assembleRelease
```

## 🤝 Contributing
1. Fork the repository
2. Create your feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add some amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

## 📝 Notes
- Ensure you have the latest Android Studio version
- Keep dependencies up to date
- Follow the project's code style
- Write tests for new features
- Document your changes

Feel free to submit issues and enhancement requests!

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
