package com.example.micelios.presentation.auth

sealed class WelcomeUiState {
    data object Idle : WelcomeUiState()
    data object Loading : WelcomeUiState()
    data object Success : WelcomeUiState()
    data class Error(val message: String) : WelcomeUiState()
}