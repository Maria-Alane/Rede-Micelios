package com.example.micelios.data.remote.dto

import com.google.firebase.Timestamp

data class HyphaMemberDocument(
    val id: String = "",
    val hyphaId: String = "",
    val userId: String = "",
    val displayName: String = "",
    val role: String = "",
    val joinedAt: Timestamp? = null
)