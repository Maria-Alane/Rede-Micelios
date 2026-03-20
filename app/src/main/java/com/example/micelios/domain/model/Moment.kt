package com.example.micelios.domain.model

data class Moment(
    val id: Long,
    val hyphaId: Long,
    val creatorUserId: Long,
    val creatorDisplayName: String,
    val content: String,
    val photoUri: String? = null,
    val timestamp: Long
)