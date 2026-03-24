package com.example.micelios.presentation.moments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.AuthRepository
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.domain.model.Hypha
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MomentListViewModel @Inject constructor(
    private val momentRepository: MomentRepository,
    private val hyphaRepository: HyphaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _hyphas = MutableStateFlow<List<Hypha>>(emptyList())
    val hyphas: StateFlow<List<Hypha>> = _hyphas

    private val _uiState = MutableStateFlow<MomentListUiState>(MomentListUiState.Idle)
    val uiState: StateFlow<MomentListUiState> = _uiState

    fun loadUserHyphas() {
        val currentUserId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            hyphaRepository.getHyphasForUser(currentUserId).collect {
                _hyphas.value = it
            }
        }
    }

    fun createMoment(hyphaId: String, content: String) {
        if (content.isBlank()) {
            _uiState.value = MomentListUiState.Error("Digite um momento.")
            return
        }

        viewModelScope.launch {
            _uiState.value = MomentListUiState.Posting

            val currentUserId = authRepository.getCurrentUserId()
            if (currentUserId == null) {
                _uiState.value = MomentListUiState.Error("Sessão inválida.")
                return@launch
            }

            momentRepository.insertMoment(
                hyphaId = hyphaId,
                creatorUserId = currentUserId,
                content = content
            ).onSuccess {
                _uiState.value = MomentListUiState.Success
            }.onFailure {
                _uiState.value = MomentListUiState.Error(
                    it.message ?: "Não foi possível publicar o momento."
                )
            }
        }
    }

    fun resetUiState() {
        _uiState.value = MomentListUiState.Idle
    }
}