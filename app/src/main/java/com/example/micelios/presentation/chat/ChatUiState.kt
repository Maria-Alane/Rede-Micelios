package com.example.micelios.presentation.chat

sealed class ChatUiState {
    data object Idle : ChatUiState()
    data object Sending : ChatUiState()
    data class Error(val message: String) : ChatUiState()
}