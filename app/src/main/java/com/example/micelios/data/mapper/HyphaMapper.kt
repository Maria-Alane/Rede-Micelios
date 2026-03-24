package com.example.micelios.data.mapper

import com.example.micelios.data.remote.dto.HyphaDocument
import com.example.micelios.domain.model.Hypha
import com.example.micelios.domain.model.HyphaType

fun HyphaDocument.toDomain(): Hypha {
    return Hypha(
        id = id,
        name = name,
        description = description ?: "",
        type = runCatching { HyphaType.valueOf(type) }.getOrDefault(HyphaType.PRIVATE),
        creatorUserId = creatorUserId,
        createdAt = createdAt?.toDate()?.time ?: 0L
    )
}