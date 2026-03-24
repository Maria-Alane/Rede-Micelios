package com.example.micelios.data.mapper

import com.example.micelios.data.remote.dto.MessageDocument
import com.example.micelios.domain.model.Message

fun MessageDocument.toDomain(): Message {
    return Message(
        id = id,
        hyphaId = hyphaId,
        senderUserId = senderUserId,
        senderDisplayName = senderDisplayName ?: "",
        content = content,
        timestamp = timestamp?.toDate()?.time ?: 0L
    )
}