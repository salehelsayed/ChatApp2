package com.example.cheatsignal.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cheatsignal.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeepLinkTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testConversationListDeepLink() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("cheatsignal://conversations"))
        composeTestRule.activity.startActivity(intent)
        composeTestRule.onNodeWithTag("conversationListScreen").assertIsDisplayed()
    }

    @Test
    fun testChatDetailDeepLink() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("cheatsignal://chat/1"))
        composeTestRule.activity.startActivity(intent)
        composeTestRule.onNodeWithTag("chatDetailScreen").assertIsDisplayed()
    }

    @Test
    fun testSettingsDeepLink() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("cheatsignal://settings"))
        composeTestRule.activity.startActivity(intent)
        composeTestRule.onNodeWithTag("settingsScreen").assertIsDisplayed()
    }

    @Test
    fun testInvalidDeepLink() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("cheatsignal://unknown"))
        composeTestRule.activity.startActivity(intent)
        composeTestRule.onNodeWithTag("conversationListScreen").assertIsDisplayed()
    }
}
