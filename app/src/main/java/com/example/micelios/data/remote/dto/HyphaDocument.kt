package com.example.micelios.data.remote.dto

import com.google.firebase.Timestamp

data class HyphaDocument(
    val id: String = "",
    val name: String = "",
    val description: String? = null,
    val type: String = "",
    val creatorUserId: String = "",
    val createdAt: Timestamp? = null
)