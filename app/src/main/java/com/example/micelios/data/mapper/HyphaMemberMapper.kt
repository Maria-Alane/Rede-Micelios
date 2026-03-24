package com.example.micelios.data.mapper

import com.example.micelios.data.remote.dto.HyphaMemberDocument
import com.example.micelios.domain.model.HyphaMember
import com.example.micelios.domain.model.HyphaRole

fun HyphaMemberDocument.toDomain(): HyphaMember {
    return HyphaMember(
        id = id,
        hyphaId = hyphaId,
        userId = userId,
        displayName = displayName,
        role = runCatching { HyphaRole.valueOf(role) }.getOrDefault(HyphaRole.MEMBER),
        joinedAt = joinedAt?.toDate()?.time ?: 0L
    )
}