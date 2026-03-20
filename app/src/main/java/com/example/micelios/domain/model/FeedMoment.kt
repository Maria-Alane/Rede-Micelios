package com.example.micelios.domain.model

data class FeedMoment(
    val id: Long,
    val hyphaId: Long,
    val hyphaName: String,
    val creatorUserId: Long,
    val creatorDisplayName: String,
    val creatorPhotoUri: String? = null,
    val content: String,
    val photoUri: String? = null,
    val timestamp: Long
)