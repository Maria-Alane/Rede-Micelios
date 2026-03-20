package com.example.micelios.data.local.relation

data class HyphaMomentRow(
    val id: Long,
    val hyphaId: Long,
    val creatorUserId: Long,
    val creatorDisplayName: String,
    val content: String,
    val photoUri: String?,
    val timestamp: Long
)