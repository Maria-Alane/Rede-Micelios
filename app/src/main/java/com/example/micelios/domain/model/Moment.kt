package com.example.micelios.domain.model

data class Moment(
    val id: String,
    val hyphaId: String,
    val creatorUserId: String,
    val creatorDisplayName: String,
    val content: String,
    val photoUri: String? = null,
    val timestamp: Long
)