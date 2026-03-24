package com.example.micelios.domain.model

data class Message(
    val id: String,
    val hyphaId: String,
    val senderUserId: String,
    val senderDisplayName: String,
    val content: String,
    val timestamp: Long
)