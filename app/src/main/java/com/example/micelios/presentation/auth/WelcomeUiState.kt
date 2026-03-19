package com.example.micelios.presentation.auth

sealed class WelcomeUiState {
    object Idle : WelcomeUiState()
    object Loading : WelcomeUiState()
    object Success : WelcomeUiState()
    data class Error(val message: String) : WelcomeUiState()
}