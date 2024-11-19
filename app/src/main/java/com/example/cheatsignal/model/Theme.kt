package com.example.cheatsignal.model

enum class Theme {
    LIGHT, DARK, SYSTEM;

    companion object {
        fun fromString(value: String): Theme {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                SYSTEM
            }
        }
    }
}
