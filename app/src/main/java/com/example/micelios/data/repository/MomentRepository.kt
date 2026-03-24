package com.example.micelios.data.repository

import com.example.micelios.domain.model.FeedMoment
import com.example.micelios.domain.model.Moment
import kotlinx.coroutines.flow.Flow

interface MomentRepository {

    fun getMomentsByHypha(hyphaId: String): Flow<List<Moment>>

    fun getFeedMomentsForUser(userId: String): Flow<List<FeedMoment>>

    suspend fun insertMoment(
        hyphaId: String,
        creatorUserId: String,
        content: String,
        photoUri: String? = null
    ): Result<String>
}