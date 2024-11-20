package com.example.cheatsignal.ui.state

/**
 * Generic UI state holder for handling loading, success, and error states
 */
sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()

    val isLoading: Boolean
        get() = this is Loading

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun getOrDefault(default: @UnsafeVariance T): T = when (this) {
        is Success -> data
        else -> default
    }

    companion object {
        fun <T> loading() = Loading
        fun <T> success(data: T) = Success(data)
        fun <T> error(message: String) = Error(message)
    }
}
