package com.example.micelios.data.repository

import com.example.micelios.data.local.dao.MomentDao
import com.example.micelios.data.local.entity.MomentEntity
import com.example.micelios.domain.model.Moment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MomentRepository(
    private val momentDao: MomentDao
) {

    fun getMomentsByHypha(hyphaId: Long): Flow<List<Moment>> {
        return momentDao.getMomentsByHypha(hyphaId).map { list ->
            list.map {
                Moment(
                    id = it.id,
                    hyphaId = it.hyphaId,
                    creatorName = it.creatorName,
                    content = it.content,
                    photoUri = it.photoUri,
                    timestamp = it.timestamp
                )
            }
        }
    }

    fun getFeedMoments(): Flow<List<Moment>> {
        val last24h = System.currentTimeMillis() - 24 * 60 * 60 * 1000
        return momentDao.getFeedMoments(last24h).map { list ->
            list.map {
                Moment(
                    id = it.id,
                    hyphaId = it.hyphaId,
                    creatorName = it.creatorName,
                    content = it.content,
                    photoUri = it.photoUri,
                    timestamp = it.timestamp
                )
            }
        }
    }

    suspend fun insertMoment(
        hyphaId: Long,
        creatorName: String,
        content: String,
        photoUri: String? = null
    ) {
        momentDao.insert(
            MomentEntity(
                hyphaId = hyphaId,
                creatorName = creatorName,
                content = content,
                photoUri = photoUri
            )
        )
    }
}