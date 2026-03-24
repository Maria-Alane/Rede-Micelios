package com.example.micelios.domain.model

data class FeedMoment(
    val id: String,
    val hyphaId: String,
    val hyphaName: String,
    val creatorUserId: String,
    val creatorDisplayName: String,
    val creatorPhotoUri: String? = null,
    val content: String,
    val photoUri: String? = null,
    val timestamp: Long
)