package com.example.cheatsignal.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import java.io.IOException
import java.net.SocketTimeoutException

@ExperimentalCoroutinesApi
class OpenAIServiceTest {
    private lateinit var service: OpenAIService
    private val mockClient: OkHttpClient = mock()
    
    @Before
    fun setup() {
        service = OpenAIService(mockClient)
    }
    
    @Test
    fun `getAIResponse should handle successful response`() = runTest {
        // Given
        val jsonResponse = """
            {
                "choices": [
                    {
                        "message": {
                            "content": "Hello, how can I help you?"
                        }
                    }
                ]
            }
        """.trimIndent()
        
        val response = Response.Builder()
            .code(200)
            .message("OK")
            .protocol(Protocol.HTTP_1_1)
            .request(any())
            .body(jsonResponse.toResponseBody("application/json".toMediaType()))
            .build()
        
        whenever(mockClient.newCall(any())).thenReturn(mock {
            on { execute() } doReturn response
        })
        
        // When
        val result = service.getAIResponse("Hello")
        
        // Then
        assertEquals("Hello, how can I help you?", result)
    }
    
    @Test(expected = IOException::class)
    fun `getAIResponse should handle network error`() = runTest {
        // Given
        whenever(mockClient.newCall(any())).thenReturn(mock {
            on { execute() } doThrow SocketTimeoutException("Network timeout")
        })
        
        // When/Then
        service.getAIResponse("Hello") // Should throw IOException
    }
    
    @Test
    fun `getAIResponse should handle rate limit response`() = runTest {
        // Given
        val response = Response.Builder()
            .code(429)
            .message("Too Many Requests")
            .protocol(Protocol.HTTP_1_1)
            .request(any())
            .body("Rate limit exceeded".toResponseBody("text/plain".toMediaType()))
            .build()
        
        whenever(mockClient.newCall(any())).thenReturn(mock {
            on { execute() } doReturn response
        })
        
        var exceptionThrown = false
        
        try {
            // When
            service.getAIResponse("Hello")
        } catch (e: IOException) {
            // Then
            assertTrue(e.message?.contains("Rate limit") == true)
            exceptionThrown = true
        }
        
        assertTrue("Expected rate limit exception", exceptionThrown)
    }
    
    @Test
    fun `getAIResponse should handle invalid response format`() = runTest {
        // Given
        val invalidJsonResponse = """
            {
                "invalid": "response"
            }
        """.trimIndent()
        
        val response = Response.Builder()
            .code(200)
            .message("OK")
            .protocol(Protocol.HTTP_1_1)
            .request(any())
            .body(invalidJsonResponse.toResponseBody("application/json".toMediaType()))
            .build()
        
        whenever(mockClient.newCall(any())).thenReturn(mock {
            on { execute() } doReturn response
        })
        
        var exceptionThrown = false
        
        try {
            // When
            service.getAIResponse("Hello")
        } catch (e: IOException) {
            // Then
            assertTrue(e.message?.contains("Invalid response format") == true)
            exceptionThrown = true
        }
        
        assertTrue("Expected invalid format exception", exceptionThrown)
    }
    
    @Test
    fun `getAIResponse should retry on temporary error`() = runTest {
        // Given
        val errorResponse = Response.Builder()
            .code(500)
            .message("Internal Server Error")
            .protocol(Protocol.HTTP_1_1)
            .request(any())
            .body("Server Error".toResponseBody("text/plain".toMediaType()))
            .build()
            
        val successResponse = Response.Builder()
            .code(200)
            .message("OK")
            .protocol(Protocol.HTTP_1_1)
            .request(any())
            .body("""{"choices":[{"message":{"content":"Success after retry"}}]}"""
                .toResponseBody("application/json".toMediaType()))
            .build()
        
        whenever(mockClient.newCall(any()))
            .thenReturn(mock { on { execute() } doReturn errorResponse })
            .thenReturn(mock { on { execute() } doReturn successResponse })
        
        // When
        val result = service.getAIResponse("Hello")
        
        // Then
        assertEquals("Success after retry", result)
        verify(mockClient, times(2)).newCall(any())
    }
}
