package com.example.micelios.domain.model

data class FeedMoment(
    val id: Long,
    val hyphaId: Long,
    val hyphaName: String,
    val creatorName: String,
    val content: String,
    val photoUri: String? = null,
    val timestamp: Long
)