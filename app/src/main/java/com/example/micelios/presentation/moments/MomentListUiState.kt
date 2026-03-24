package com.example.micelios.presentation.moments

sealed class MomentListUiState {
    data object Idle : MomentListUiState()
    data object Posting : MomentListUiState()
    data object Success : MomentListUiState()
    data class Error(val message: String) : MomentListUiState()
}