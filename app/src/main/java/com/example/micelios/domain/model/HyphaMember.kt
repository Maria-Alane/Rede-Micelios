package com.example.micelios.domain.model

data class HyphaMember(
    val id: Long,
    val hyphaId: Long,
    val userId: Long,
    val displayName: String,
    val role: HyphaRole,
    val joinedAt: Long
)