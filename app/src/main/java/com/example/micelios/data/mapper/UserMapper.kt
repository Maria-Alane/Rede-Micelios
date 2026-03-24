package com.example.micelios.data.mapper

import com.example.micelios.data.remote.dto.UserDocument
import com.example.micelios.domain.model.User

fun UserDocument.toDomain(): User {
    return User(
        id = id,
        name = name,
        bio = bio ?: "",
        photoUri = photoUri,
        memberSince = createdAt?.toDate()?.time ?: 0L
    )
}