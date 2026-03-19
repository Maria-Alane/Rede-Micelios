package com.example.micelios.presentation.hypha

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MemberDraft
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.domain.model.Hypha
import com.example.micelios.domain.model.HyphaType
import com.example.micelios.presentation.common.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HyphaListViewModel(
    private val hyphaRepository: HyphaRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _hyphas = MutableStateFlow<List<Hypha>>(emptyList())
    val hyphas: StateFlow<List<Hypha>> = _hyphas

    fun loadHyphas() {
        val currentUserId = sessionManager.getCurrentUserId() ?: return

        viewModelScope.launch {
            hyphaRepository.getHyphasForUser(currentUserId).collect { list ->
                _hyphas.value = list
            }
        }
    }

    fun createHypha(
        name: String,
        description: String,
        type: HyphaType,
        additionalMembers: List<MemberDraft>
    ) {
        if (name.isBlank()) return

        viewModelScope.launch {
            val currentUserId = sessionManager.getCurrentUserId() ?: return@launch
            val currentUser = userRepository.getUserByIdOnce(currentUserId) ?: return@launch

            val allMembers = buildList {
                add(MemberDraft(currentUser.id, currentUser.name))
                addAll(additionalMembers)
            }.distinctBy { it.userId }

            if (allMembers.size < 2) {
                return@launch
            }

            hyphaRepository.createHypha(
                name = name,
                description = description,
                type = type,
                creatorUserId = currentUser.id,
                creatorDisplayName = currentUser.name,
                members = allMembers
            )
        }
    }
}