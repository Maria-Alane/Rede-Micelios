package com.example.micelios.data.repository

import com.example.micelios.data.local.dao.MessageDao
import com.example.micelios.data.local.entity.MessageEntity
import com.example.micelios.domain.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepository(private val messageDao: MessageDao) {

    fun getMessagesByHypha(hyphaId: Long): Flow<List<Message>> {
        return messageDao.getMessagesByHypha(hyphaId).map { list ->
            list.map {
                Message(
                    id = it.id,
                    hyphaId = it.hyphaId,
                    senderName = it.senderName,
                    content = it.content,
                    timestamp = it.timestamp
                )
            }
        }
    }

    suspend fun insertMessage(
        hyphaId: Long,
        senderName: String,
        content: String
    ) {
        messageDao.insert(
            MessageEntity(
                hyphaId = hyphaId,
                senderName = senderName,
                content = content
            )
        )
    }
}