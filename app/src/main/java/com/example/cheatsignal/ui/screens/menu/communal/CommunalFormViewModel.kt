package com.example.cheatsignal.ui.screens.menu.communal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatsignal.data.model.CommunalAddress
import com.example.cheatsignal.data.repository.CommunalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunalFormViewModel @Inject constructor(
    private val communalRepository: CommunalRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommunalFormState())
    val uiState: StateFlow<CommunalFormState> = _uiState.asStateFlow()

    fun updateField(field: String, value: String) {
        _uiState.update { currentState ->
            when (field) {
                "addressLine" -> currentState.copy(addressLine = value)
                "locality" -> currentState.copy(locality = value)
                "postalCode" -> currentState.copy(postalCode = value)
                "country" -> currentState.copy(country = value)
                else -> currentState
            }.copy(errors = currentState.errors - field)
        }
    }

    fun validateForm(): Boolean {
        val errors = mutableMapOf<String, String>()
        with(uiState.value) {
            if (addressLine.isBlank()) errors["addressLine"] = "Address is required"
            if (locality.isBlank()) errors["locality"] = "City/Town is required"
            if (postalCode.isBlank()) errors["postalCode"] = "Postal code is required"
            if (country.isBlank()) errors["country"] = "Country is required"
        }
        _uiState.update { it.copy(errors = errors) }
        return errors.isEmpty()
    }

    fun saveAddress() {
        if (!validateForm()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                with(uiState.value) {
                    communalRepository.saveAddress(
                        CommunalAddress(
                            addressLine = addressLine,
                            locality = locality,
                            postalCode = postalCode,
                            country = country
                        )
                    )
                    _uiState.update { it.copy(isSaved = true) }
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
