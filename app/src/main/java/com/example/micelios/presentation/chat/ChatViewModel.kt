package com.example.micelios.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.MessageRepository
import com.example.micelios.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Idle)
    val uiState: StateFlow<ChatUiState> = _uiState

    fun loadMessages(hyphaId: String) {
        viewModelScope.launch {
            messageRepository.getMessagesByHypha(hyphaId).collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(
        hyphaId: String,
        senderUserId: String,
        content: String
    ) {
        if (content.isBlank()) {
            _uiState.value = ChatUiState.Error("Digite uma mensagem.")
            return
        }

        viewModelScope.launch {
            _uiState.value = ChatUiState.Sending

            messageRepository.insertMessage(
                hyphaId = hyphaId,
                senderUserId = senderUserId,
                content = content
            ).onSuccess {
                _uiState.value = ChatUiState.Idle
            }.onFailure {
                _uiState.value = ChatUiState.Error(
                    it.message ?: "Não foi possível enviar a mensagem."
                )
            }
        }
    }

    fun resetUiState() {
        _uiState.value = ChatUiState.Idle
    }
}