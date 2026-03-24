package com.example.micelios.data.remote.dto

import com.google.firebase.Timestamp

data class UserDocument(
    val id: String = "",
    val name: String = "",
    val email: String? = null,
    val emailLowercase: String? = null,
    val bio: String? = null,
    val photoUri: String? = null,
    val createdAt: Timestamp? = null
)