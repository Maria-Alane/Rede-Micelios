package com.example.micelios.domain.model

data class HyphaMember(
    val id: String,
    val hyphaId: String,
    val userId: String,
    val displayName: String,
    val role: HyphaRole,
    val joinedAt: Long
)