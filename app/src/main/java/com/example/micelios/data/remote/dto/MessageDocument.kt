package com.example.micelios.data.remote.dto

import com.google.firebase.Timestamp

data class MessageDocument(
    val id: String = "",
    val hyphaId: String = "",
    val senderUserId: String = "",
    val senderDisplayName: String? = null,
    val content: String = "",
    val timestamp: Timestamp? = null
)