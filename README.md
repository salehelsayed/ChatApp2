# Maktoub Chat App

A modern Android chat application built with Jetpack Compose and Material Design 3, inspired by Signal's clean and minimalist UI.

## Project Overview

Maktoub is a chat application that focuses on providing a smooth, intuitive user experience with a clean, modern interface. Built entirely with Kotlin and Jetpack Compose, it follows Material Design 3 guidelines for a polished look and feel.

## Features

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

### Planned
- Settings screen
- Screen transition animations
- Dark theme support
- Profile management
- Message sending functionality
- Accessibility improvements

## Technical Details

### Development Environment
- Minimum SDK: 24
- Target SDK: 34
- Kotlin Version: 1.9.0
- Compose BOM: 2024.01.00

### Key Dependencies
- Jetpack Compose
- Material Design 3
- AndroidX Core KTX
- Lifecycle Runtime KTX

## Project Structure
```
app/src/main/
├── java/com/example/cheatsignal/
│   ├── model/
│   │   ├── Conversation.kt
│   │   └── Message.kt
│   ├── ui/
│   │   ├── components/
│   │   │   ├── ConversationItem.kt
│   │   │   └── MessageBubble.kt
│   │   ├── screens/
│   │   │   ├── ChatDetailScreen.kt
│   │   │   └── ConversationListScreen.kt
│   │   └── theme/
│   │       ├── Color.kt
│   │       ├── Theme.kt
│   │       └── Type.kt
│   └── MainActivity.kt
└── res/
    └── ...
```

## UI Components

### Conversation List Screen
- Displays list of conversations
- Shows user avatar, name, last message, and timestamp
- Unread message counter
- Search and menu buttons in the top bar
- Floating action button for new chats

### Chat Detail Screen
- Message bubbles with different styles for sent/received
- Message status indicators
- Timestamp for each message
- Input field with attachment and emoji options
- Proper keyboard handling and animations

## Current Status
- Version: 0.1.0
- Basic UI implementation complete
- Using mock data for development
- Focus on UI/UX polish

## Getting Started

### Prerequisites
- Android Studio Latest Version
- Android SDK
- JDK 11 or higher

### Building the Project
1. Clone the repository
```bash
git clone https://github.com/salehelsayed/ChatApp2.git
```
2. Open in Android Studio
3. Build and run on an emulator or physical device

## Contributing
Feel free to submit issues and enhancement requests!
