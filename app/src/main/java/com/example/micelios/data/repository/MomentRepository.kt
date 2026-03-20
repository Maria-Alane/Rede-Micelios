package com.example.micelios.data.repository

import com.example.micelios.data.local.dao.HyphaDao
import com.example.micelios.data.local.dao.MomentDao
import com.example.micelios.data.local.entity.MomentEntity
import com.example.micelios.domain.model.FeedMoment
import com.example.micelios.domain.model.Moment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MomentRepository(
    private val momentDao: MomentDao,
    private val hyphaDao: HyphaDao
) {

    fun getMomentsByHypha(hyphaId: Long): Flow<List<Moment>> {
        return momentDao.getMomentsByHypha(hyphaId).map { list ->
            list.map {
                Moment(
                    id = it.id,
                    hyphaId = it.hyphaId,
                    creatorUserId = it.creatorUserId,
                    creatorDisplayName = it.creatorDisplayName,
                    content = it.content,
                    photoUri = it.photoUri,
                    timestamp = it.timestamp
                )
            }
        }
    }

    fun getFeedMomentsForUser(userId: Long): Flow<List<FeedMoment>> {
        val last24h = System.currentTimeMillis() - 24 * 60 * 60 * 1000

        return momentDao.getFeedMomentsForUser(
            currentUserId = userId,
            fromTimestamp = last24h
        ).map { list ->
            list.map {
                FeedMoment(
                    id = it.id,
                    hyphaId = it.hyphaId,
                    hyphaName = it.hyphaName,
                    creatorUserId = it.creatorUserId,
                    creatorDisplayName = it.creatorDisplayName,
                    content = it.content,
                    photoUri = it.photoUri,
                    timestamp = it.timestamp
                )
            }
        }
    }

    suspend fun insertMoment(
        hyphaId: Long,
        creatorUserId: Long,
        content: String,
        photoUri: String? = null
    ) {
        momentDao.insert(
            MomentEntity(
                hyphaId = hyphaId,
                creatorUserId = creatorUserId,
                content = content,
                photoUri = photoUri
            )
        )
    }
}