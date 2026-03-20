package com.example.micelios.data.local.relation

data class ChatMessageRow(
    val id: Long,
    val hyphaId: Long,
    val senderUserId: Long,
    val senderDisplayName: String,
    val content: String,
    val timestamp: Long
)