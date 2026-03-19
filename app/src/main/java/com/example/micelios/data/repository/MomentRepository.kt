package com.example.micelios.data.repository

import com.example.micelios.data.local.dao.HyphaDao
import com.example.micelios.data.local.dao.MomentDao
import com.example.micelios.data.local.entity.MomentEntity
import com.example.micelios.domain.model.FeedMoment
import com.example.micelios.domain.model.Moment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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
                    creatorName = it.creatorName,
                    content = it.content,
                    photoUri = it.photoUri,
                    timestamp = it.timestamp
                )
            }
        }
    }

    fun getFeedMomentsForUser(userId: Long): Flow<List<FeedMoment>> {
        val last24h = System.currentTimeMillis() - 24 * 60 * 60 * 1000

        return combine(
            momentDao.getFeedMoments(last24h),
            hyphaDao.getHyphasForUser(userId)
        ) { moments, hyphas ->
            val hyphaMap = hyphas.associateBy { it.id }

            moments
                .filter { hyphaMap.containsKey(it.hyphaId) }
                .map { moment ->
                    FeedMoment(
                        id = moment.id,
                        hyphaId = moment.hyphaId,
                        hyphaName = hyphaMap[moment.hyphaId]?.name ?: "Hypha",
                        creatorName = moment.creatorName,
                        content = moment.content,
                        photoUri = moment.photoUri,
                        timestamp = moment.timestamp
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