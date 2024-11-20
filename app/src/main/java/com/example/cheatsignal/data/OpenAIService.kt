package com.example.cheatsignal.data

import android.content.Context
import android.util.Log
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Properties
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenAIService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var openAI: OpenAI? = null
    private var systemPrompt: String = ""

    init {
        try {
            val properties = Properties()
            context.assets.open("config.properties").use { 
                properties.load(it)
            }
            val apiKey = properties.getProperty("OPENAI_API_KEY")
            if (!apiKey.isNullOrBlank()) {
                openAI = OpenAI(apiKey)
            }
            
            try {
                systemPrompt = context.assets.open("ai_prompt.txt").bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                Log.e("OpenAIService", "Error loading AI prompt: ${e.message}")
                systemPrompt = "You are Alice AI, a friendly and helpful assistant."
            }
        } catch (e: Exception) {
            Log.e("OpenAIService", "Error initializing OpenAI service: ${e.message}")
        }
    }

    suspend fun sendMessage(message: String): Flow<String> = flow {
        try {
            val api = openAI
            if (api == null) {
                emit("I'm sorry, but I'm currently unable to process messages. Please try again later. ")
                return@flow
            }

            val chatCompletionRequest = ChatCompletionRequest(
                model = ModelId("gpt-4"),
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.System,
                        content = systemPrompt
                    ),
                    ChatMessage(
                        role = ChatRole.User,
                        content = message
                    )
                )
            )

            try {
                val completion: ChatCompletion = api.chatCompletion(chatCompletionRequest)
                val response = completion.choices.firstOrNull()?.message?.content
                if (response.isNullOrBlank()) {
                    emit("I apologize, but I couldn't generate a proper response. Please try again. ")
                } else {
                    emit(response)
                }
            } catch (e: Exception) {
                Log.e("OpenAIService", "Error during API call: ${e.message}")
                emit("I encountered an error while processing your message. Please try again later. ")
            }
        } catch (e: Exception) {
            Log.e("OpenAIService", "Error in sendMessage: ${e.message}")
            emit("I'm having trouble connecting right now. Please try again later. ")
        }
    }
}
