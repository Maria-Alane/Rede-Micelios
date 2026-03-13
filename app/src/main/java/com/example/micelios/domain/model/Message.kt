package com.example.micelios.domain.model

data class Message(
    val id: Long,
    val hyphaId: Long,
    val senderName: String,
    val content: String,
    val timestamp: Long
)