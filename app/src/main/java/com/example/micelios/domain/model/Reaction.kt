package com.example.micelios.domain.model

data class Reaction(
    val id: Long,
    val momentId: Long,
    val userName: String,
    val type: String
)