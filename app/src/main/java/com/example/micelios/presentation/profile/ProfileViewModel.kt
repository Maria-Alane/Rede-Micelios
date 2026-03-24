package com.example.micelios.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.AuthRepository
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.domain.model.FeedMoment
import com.example.micelios.domain.model.Hypha
import com.example.micelios.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val hyphaRepository: HyphaRepository,
    private val momentRepository: MomentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _hyphas = MutableStateFlow<List<Hypha>>(emptyList())
    val hyphas: StateFlow<List<Hypha>> = _hyphas

    private val _moments = MutableStateFlow<List<FeedMoment>>(emptyList())
    val moments: StateFlow<List<FeedMoment>> = _moments

    private var hasLoaded = false

    fun loadProfile() {
        if (hasLoaded) return

        val currentUserId = authRepository.getCurrentUserId() ?: return
        hasLoaded = true

        viewModelScope.launch {
            userRepository.getUserById(currentUserId).collect { _user.value = it }
        }

        viewModelScope.launch {
            hyphaRepository.getHyphasForUser(currentUserId).collect { _hyphas.value = it }
        }

        viewModelScope.launch {
            momentRepository.getFeedMomentsForUser(currentUserId).collect { feedMoments ->
                _moments.value = feedMoments.filter { it.creatorUserId == currentUserId }
            }
        }
    }
}