package com.example.micelios.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.MessageRepository
import com.example.micelios.domain.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun loadMessages(hyphaId: Long) {
        viewModelScope.launch {
            messageRepository.getMessagesByHypha(hyphaId).collect { list ->
                _messages.value = list
            }
        }
    }

    fun sendMessage(hyphaId: Long, senderUserId: Long, content: String) {
        if (content.isBlank()) return

        viewModelScope.launch {
            messageRepository.insertMessage(
                hyphaId = hyphaId,
                senderUserId = senderUserId,
                content = content
            )
        }
    }
}