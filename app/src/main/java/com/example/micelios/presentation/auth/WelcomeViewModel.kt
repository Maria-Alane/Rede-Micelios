package com.example.micelios.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.AuthRepository
import com.example.micelios.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WelcomeUiState>(WelcomeUiState.Idle)
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    fun checkExistingSession() {
        if (authRepository.isLoggedIn()) {
            _uiState.value = WelcomeUiState.Success
        }
    }

    fun register(name: String, email: String, password: String) {
        val trimmedName = name.trim()
        val trimmedEmail = email.trim()

        if (trimmedName.length < 2) {
            _uiState.value = WelcomeUiState.Error("O nome deve ter pelo menos 2 caracteres.")
            return
        }

        if (trimmedEmail.isBlank()) {
            _uiState.value = WelcomeUiState.Error("Digite seu e-mail.")
            return
        }

        if (password.length < 6) {
            _uiState.value = WelcomeUiState.Error("A senha deve ter pelo menos 6 caracteres.")
            return
        }

        viewModelScope.launch {
            _uiState.value = WelcomeUiState.Loading

            authRepository.signUp(trimmedEmail, password)
                .onSuccess { uid ->
                    userRepository.createUser(
                        id = uid,
                        name = trimmedName,
                        email = trimmedEmail
                    ).onSuccess {
                        _uiState.value = WelcomeUiState.Success
                    }.onFailure {
                        authRepository.signOut()
                        _uiState.value = WelcomeUiState.Error(
                            "Conta criada, mas não foi possível salvar o perfil."
                        )
                    }
                }
                .onFailure {
                    _uiState.value = WelcomeUiState.Error(
                        it.message ?: "Não foi possível criar sua conta agora."
                    )
                }
        }
    }

    fun signIn(email: String, password: String) {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank()) {
            _uiState.value = WelcomeUiState.Error("Digite seu e-mail.")
            return
        }

        if (password.isBlank()) {
            _uiState.value = WelcomeUiState.Error("Digite sua senha.")
            return
        }

        viewModelScope.launch {
            _uiState.value = WelcomeUiState.Loading

            authRepository.signIn(trimmedEmail, password)
                .onSuccess {
                    _uiState.value = WelcomeUiState.Success
                }
                .onFailure {
                    _uiState.value = WelcomeUiState.Error(
                        it.message ?: "Não foi possível entrar agora."
                    )
                }
        }
    }

    fun resetState() {
        _uiState.value = WelcomeUiState.Idle
    }
}