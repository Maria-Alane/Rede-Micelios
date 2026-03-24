package com.example.micelios.data.mapper

import com.example.micelios.data.remote.dto.MomentDocument
import com.example.micelios.domain.model.FeedMoment
import com.example.micelios.domain.model.Moment

fun MomentDocument.toMomentDomain(): Moment {
    return Moment(
        id = id,
        hyphaId = hyphaId,
        creatorUserId = creatorUserId,
        creatorDisplayName = creatorDisplayName ?: "",
        content = content,
        photoUri = photoUri,
        timestamp = timestamp?.toDate()?.time ?: 0L
    )
}

fun MomentDocument.toFeedDomain(): FeedMoment {
    return FeedMoment(
        id = id,
        hyphaId = hyphaId,
        hyphaName = hyphaName,
        creatorUserId = creatorUserId,
        creatorDisplayName = creatorDisplayName ?: "",
        creatorPhotoUri = creatorPhotoUri,
        content = content,
        photoUri = photoUri,
        timestamp = timestamp?.toDate()?.time ?: 0L
    )
}