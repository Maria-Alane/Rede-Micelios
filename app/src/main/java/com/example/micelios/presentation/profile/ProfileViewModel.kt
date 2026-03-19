package com.example.micelios.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.domain.model.User
import com.example.micelios.presentation.common.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadUser() {
        val currentUserId = sessionManager.getCurrentUserId() ?: return

        viewModelScope.launch {
            userRepository.getUserById(currentUserId).collect { user ->
                _user.value = user
            }
        }
    }
}