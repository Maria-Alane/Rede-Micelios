package com.example.micelios.presentation.hypha

sealed class CreateHyphaUiState {
    data object Idle : CreateHyphaUiState()
    data object Loading : CreateHyphaUiState()
    data object Success : CreateHyphaUiState()
    data class Error(val message: String) : CreateHyphaUiState()
}