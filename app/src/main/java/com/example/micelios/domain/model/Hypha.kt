package com.example.micelios.domain.model

data class Hypha(
    val id: Long,
    val name: String,
    val description: String,
    val type: HyphaType,
    val creatorUserId: Long,
    val createdAt: Long
)