package com.example.micelios.data.local.relation

data class FeedMomentRow(
    val id: Long,
    val hyphaId: Long,
    val hyphaName: String,
    val creatorUserId: Long,
    val creatorDisplayName: String,
    val content: String,
    val photoUri: String?,
    val timestamp: Long
)