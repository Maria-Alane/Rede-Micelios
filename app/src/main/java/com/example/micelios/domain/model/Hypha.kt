package com.example.micelios.domain.model

data class Hypha(
    val id: String,
    val name: String,
    val description: String,
    val type: HyphaType,
    val creatorUserId: String,
    val createdAt: Long
)