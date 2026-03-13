package com.example.micelios.domain.model

data class User(
    val id: Long,
    val name: String,
    val bio: String,
    val photoUri: String? = null,
    val memberSince: Long
)