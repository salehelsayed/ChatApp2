package com.example.cheatsignal.ui.screens.menu.communal

data class CommunalFormState(
    val addressLine: String = "",
    val locality: String = "",
    val postalCode: String = "",
    val country: String = "",
    val errors: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)
