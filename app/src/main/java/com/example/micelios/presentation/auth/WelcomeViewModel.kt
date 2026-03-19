package com.example.micelios.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.presentation.common.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<WelcomeUiState>(WelcomeUiState.Idle)
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    fun registerLocalUser(name: String) {
        val trimmedName = name.trim()

        if (trimmedName.isBlank()) {
            _uiState.value = WelcomeUiState.Error("Digite seu nome para continuar.")
            return
        }

        if (trimmedName.length < 2) {
            _uiState.value = WelcomeUiState.Error("O nome deve ter pelo menos 2 caracteres.")
            return
        }

        viewModelScope.launch {
            _uiState.value = WelcomeUiState.Loading

            try {
                val userId = userRepository.createUser(name = trimmedName)
                sessionManager.saveCurrentUserId(userId)
                _uiState.value = WelcomeUiState.Success
            } catch (e: Exception) {
                _uiState.value = WelcomeUiState.Error("Não foi possível criar seu perfil agora.")
            }
        }
    }

    fun resetState() {
        _uiState.value = WelcomeUiState.Idle
    }
}