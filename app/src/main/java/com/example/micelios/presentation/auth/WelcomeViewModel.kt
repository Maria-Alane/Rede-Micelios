package com.example.micelios.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _hasUser = MutableStateFlow(false)
    val hasUser: StateFlow<Boolean> = _hasUser

    fun checkUser() {
        viewModelScope.launch {
            userRepository.getUser().collect { user ->
                _hasUser.value = user != null
            }
        }
    }

    fun saveUser(name: String, bio: String = "", photoUri: String? = null) {
        if (name.isBlank()) return

        viewModelScope.launch {
            userRepository.saveUser(
                name = name,
                bio = bio,
                photoUri = photoUri
            )
        }
    }
}