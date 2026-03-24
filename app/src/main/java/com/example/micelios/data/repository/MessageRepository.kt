package com.example.micelios.data.repository

import com.example.micelios.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun getMessagesByHypha(hyphaId: String): Flow<List<Message>>

    suspend fun insertMessage(
        hyphaId: String,
        senderUserId: String,
        content: String
    ): Result<String>
}