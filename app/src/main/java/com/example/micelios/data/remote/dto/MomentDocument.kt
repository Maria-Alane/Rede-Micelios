package com.example.micelios.data.remote.dto

import com.google.firebase.Timestamp

data class MomentDocument(
    val id: String = "",
    val hyphaId: String = "",
    val hyphaName: String = "",
    val creatorUserId: String = "",
    val creatorDisplayName: String? = null,
    val creatorPhotoUri: String? = null,
    val visibleToUserIds: List<String> = emptyList(),
    val content: String = "",
    val photoUri: String? = null,
    val timestamp: Timestamp? = null
)