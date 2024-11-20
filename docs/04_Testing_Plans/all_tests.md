# CheatSignal Test Suite Overview

This document lists all the tests in the CheatSignal project and their corresponding Gradle commands to run them individually.

## UI Tests (androidTest)

### ConversationListScreenTest (9 tests)
```bash
# Show conversations test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ConversationListScreenTest.testConversationListShowsConversations"

# Show unread count test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ConversationListScreenTest.testConversationListShowsUnreadCount"

# Show last message test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ConversationListScreenTest.testConversationListShowsLastMessage"

# Empty state test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ConversationListScreenTest.testEmptyState"

# Loading state test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ConversationListScreenTest.testLoadingState"

# Conversation click test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ConversationListScreenTest.testConversationClick"

# New chat button test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ConversationListScreenTest.testNewChatButton"

# Error state test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ConversationListScreenTest.testErrorState"

# Search functionality test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ConversationListScreenTest.testSearchFunctionality"
```

### ChatScreenTest (7 tests)
```bash
# Show messages test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ChatScreenTest.testChatDetailScreenShowsMessages"

# Show loading state test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ChatScreenTest.testChatDetailScreenShowsLoadingState"

# Show error test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ChatScreenTest.testChatDetailScreenShowsError"

# Send message button test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ChatScreenTest.testSendMessageButton"

# Back button navigation test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ChatScreenTest.testBackButtonNavigation"

# Empty message validation test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ChatScreenTest.testEmptyMessageValidation"

# Long message truncation test
gradlew :app:connectedAndroidTest --tests "com.example.cheatsignal.ui.screens.ChatScreenTest.testLongMessageTruncation"
```

## Unit Tests (test)

### OpenAIServiceTest (2 tests)
```bash
# Successful response test
gradlew :app:test --tests "com.example.cheatsignal.data.OpenAIServiceTest.getAIResponse should handle successful response"

# Network error test
gradlew :app:test --tests "com.example.cheatsignal.data.OpenAIServiceTest.getAIResponse should handle network error"
```

### SettingsDtoTest (1 test)
```bash
# Equals comparison test
gradlew :app:test --tests "com.example.cheatsignal.data.model.SettingsDtoTest.test_equals_comparesAllFields"
```

### SettingsTest (1 test)
```bash
# Copy settings test
gradlew :app:test --tests "com.example.cheatsignal.domain.model.SettingsTest.copy creates new Settings with updated values"
```

## Summary
- Total Tests: 20
  - UI Tests: 16
    - ConversationListScreenTest: 9
    - ChatScreenTest: 7
  - Unit Tests: 4
    - OpenAIServiceTest: 2
    - SettingsDtoTest: 1
    - SettingsTest: 1
