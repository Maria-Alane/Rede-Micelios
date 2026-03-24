package com.example.micelios.presentation.hypha

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.AuthRepository
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MemberDraft
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.domain.model.HyphaType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateHyphaViewModel @Inject constructor(
    private val hyphaRepository: HyphaRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _inviteLookupState =
        MutableStateFlow<InviteLookupUiState>(InviteLookupUiState.Idle)
    val inviteLookupState: StateFlow<InviteLookupUiState> = _inviteLookupState

    private val _uiState = MutableStateFlow<CreateHyphaUiState>(CreateHyphaUiState.Idle)
    val uiState: StateFlow<CreateHyphaUiState> = _uiState

    fun searchMemberByEmail(email: String) {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank()) {
            _inviteLookupState.value = InviteLookupUiState.Error("Digite um e-mail.")
            return
        }

        viewModelScope.launch {
            _inviteLookupState.value = InviteLookupUiState.Loading

            val foundUser = userRepository.getUserByEmail(trimmedEmail)

            _inviteLookupState.value = if (foundUser != null) {
                InviteLookupUiState.Found(
                    member = MemberDraft(
                        userId = foundUser.id,
                        displayName = foundUser.name
                    ),
                    email = trimmedEmail
                )
            } else {
                InviteLookupUiState.NotFound
            }
        }
    }

    fun clearInviteLookup() {
        _inviteLookupState.value = InviteLookupUiState.Idle
    }

    fun createHypha(
        name: String,
        description: String,
        invitedMember: MemberDraft?
    ) {
        if (name.trim().isBlank()) {
            _uiState.value = CreateHyphaUiState.Error("Digite o nome da hypha.")
            return
        }

        if (invitedMember == null) {
            _uiState.value = CreateHyphaUiState.Error("Busque e selecione um membro por e-mail.")
            return
        }

        viewModelScope.launch {
            _uiState.value = CreateHyphaUiState.Loading

            val currentUserId = authRepository.getCurrentUserId()
            if (currentUserId == null) {
                _uiState.value = CreateHyphaUiState.Error("Sessão inválida.")
                return@launch
            }

            val currentUser = userRepository.getUserByIdOnce(currentUserId)
            if (currentUser == null) {
                _uiState.value = CreateHyphaUiState.Error("Não foi possível carregar o usuário.")
                return@launch
            }

            hyphaRepository.createHypha(
                name = name,
                description = description,
                type = HyphaType.PRIVATE,
                creatorUserId = currentUser.id,
                creatorDisplayName = currentUser.name,
                members = listOf(invitedMember)
            ).onSuccess {
                _uiState.value = CreateHyphaUiState.Success
            }.onFailure {
                _uiState.value = CreateHyphaUiState.Error(
                    it.message ?: "Não foi possível criar a hypha."
                )
            }
        }
    }

    fun resetUiState() {
        _uiState.value = CreateHyphaUiState.Idle
    }
}