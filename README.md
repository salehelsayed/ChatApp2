# Maktoub Chat App

A modern Android chat application built with Jetpack Compose and Material Design 3, inspired by Signal's clean and minimalist UI.

## 🚀 Project Overview

### Tech Stack
- **Platform**: Android
- **Language**: Kotlin
- **Minimum SDK**: 24
- **Target SDK**: 34
- **UI Framework**: Jetpack Compose
- **Design System**: Material Design 3
- **Architecture**: MVVM with Clean Architecture
- **State Management**: StateFlow, ViewModel
- **Dependency Injection**: Coming soon
- **Testing**: Coming soon

### Key Dependencies
```kotlin
// Core Dependencies
implementation(platform("androidx.compose:compose-bom:2024.01.00"))
implementation("androidx.compose.material3:material3")
implementation("androidx.navigation:navigation-compose:2.7.6")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

// UI Dependencies
implementation("androidx.compose.material:material-icons-core")
implementation("androidx.compose.material:material-icons-extended")

// Data Management (Coming Soon)
implementation("androidx.datastore:datastore-preferences:1.0.0")
```

## 🎯 Features

### Implemented
- Modern UI built with Jetpack Compose
- Material Design 3 theming
- Conversation list screen with:
  - User avatars
  - Last message preview
  - Timestamp
  - Unread message counter
- Chat detail screen with:
  - Message bubbles
  - Message status indicators (sent, delivered, read)
  - Responsive input field
  - Proper keyboard handling
  - Attachment and emoji buttons
- Smooth navigation between screens
- Edge-to-edge design

### In Progress
- Settings screen
- Screen transition animations
- Dark theme support
- Profile management
- Message sending functionality
- Accessibility improvements

### Planned
- Chat features
  - Message composition
  - Message history
  - Real-time updates
- User management
  - Authentication
  - Profile management
  - User settings

## 🏗 Architecture

### UI Layer
- **Screens**: Composable UI components
- **ViewModels**: State management and business logic
- **State**: Immutable state objects with StateFlow

### Domain Layer (Planned)
- Use cases
- Domain models
- Repository interfaces

### Data Layer (Planned)
- Repositories
- Data sources
- DTOs

## 🎨 Design System

### Material Design 3 Implementation
- Custom color scheme
- Typography system
- Component theming
- Dark mode support

### Accessibility
- Content descriptions
- Touch target sizing
- Screen reader support

## 🧪 Testing Strategy

### Unit Tests (Planned)
- ViewModel testing
- Use case testing
- Repository testing

### UI Tests (Planned)
- Component testing
- Screen navigation
- User interactions

### Integration Tests (Planned)
- End-to-end flows
- Data persistence
- Network operations

## 🚀 Getting Started

### Prerequisites
- Android Studio Latest Version
- Android SDK
- JDK 11 or higher

### Setup
1. Clone the repository
```bash
git clone https://github.com/salehelsayed/ChatApp2.git
```

2. Open in Android Studio

3. Build and run on an emulator or physical device

## 📱 Screenshots
(Coming soon)

## 🤝 Contributing
Feel free to submit issues and enhancement requests!

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
