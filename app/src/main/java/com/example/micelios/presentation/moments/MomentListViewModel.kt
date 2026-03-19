package com.example.micelios.presentation.moments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.domain.model.Hypha
import com.example.micelios.presentation.common.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MomentListViewModel(
    private val momentRepository: MomentRepository,
    private val hyphaRepository: HyphaRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _hyphas = MutableStateFlow<List<Hypha>>(emptyList())
    val hyphas: StateFlow<List<Hypha>> = _hyphas

    fun loadUserHyphas() {
        val currentUserId = sessionManager.getCurrentUserId() ?: return

        viewModelScope.launch {
            hyphaRepository.getHyphasForUser(currentUserId).collect {
                _hyphas.value = it
            }
        }
    }

    fun createMoment(
        hyphaId: Long,
        content: String
    ) {
        if (content.isBlank()) return

        viewModelScope.launch {
            val currentUserId = sessionManager.getCurrentUserId() ?: return@launch
            val currentUser = userRepository.getUserByIdOnce(currentUserId) ?: return@launch

            momentRepository.insertMoment(
                hyphaId = hyphaId,
                creatorName = currentUser.name,
                content = content
            )
        }
    }
}