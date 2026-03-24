package com.example.micelios.domain.model

data class User(
    val id: String,
    val name: String,
    val bio: String,
    val photoUri: String? = null,
    val memberSince: Long
)