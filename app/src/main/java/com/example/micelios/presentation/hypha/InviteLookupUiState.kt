package com.example.micelios.presentation.hypha

import com.example.micelios.data.repository.MemberDraft

sealed class InviteLookupUiState {
    data object Idle : InviteLookupUiState()
    data object Loading : InviteLookupUiState()
    data class Found(
        val member: MemberDraft,
        val email: String
    ) : InviteLookupUiState()
    data object NotFound : InviteLookupUiState()
    data class Error(val message: String) : InviteLookupUiState()
}