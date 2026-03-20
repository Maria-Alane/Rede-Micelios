package com.example.micelios.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.micelios.data.local.entity.MomentEntity
import com.example.micelios.data.local.relation.FeedMomentRow
import com.example.micelios.data.local.relation.HyphaMomentRow
import kotlinx.coroutines.flow.Flow

@Dao
interface MomentDao {

    @Insert
    suspend fun insert(moment: MomentEntity)

    @Query("""
        SELECT
            m.id AS id,
            m.hyphaId AS hyphaId,
            m.creatorUserId AS creatorUserId,
            hm.displayName AS creatorDisplayName,
            m.content AS content,
            m.photoUri AS photoUri,
            m.timestamp AS timestamp
        FROM moment_table m
        INNER JOIN hypha_member_table hm
            ON hm.hyphaId = m.hyphaId
           AND hm.userId = m.creatorUserId
        WHERE m.hyphaId = :hyphaId
        ORDER BY m.timestamp DESC
    """)
    fun getMomentsByHypha(hyphaId: Long): Flow<List<HyphaMomentRow>>

    @Query("""
        SELECT
            m.id AS id,
            m.hyphaId AS hyphaId,
            h.name AS hyphaName,
            m.creatorUserId AS creatorUserId,
            hm.displayName AS creatorDisplayName,
            u.photoUri AS creatorPhotoUri,
            m.content AS content,
            m.photoUri AS photoUri,
            m.timestamp AS timestamp
        FROM moment_table m
        INNER JOIN hypha_table h
            ON h.id = m.hyphaId
        INNER JOIN hypha_member_table hm
            ON hm.hyphaId = m.hyphaId
           AND hm.userId = m.creatorUserId
        INNER JOIN hypha_member_table viewer
            ON viewer.hyphaId = m.hyphaId
        INNER JOIN user_table u
            ON u.id = m.creatorUserId
        WHERE viewer.userId = :currentUserId
          AND m.timestamp >= :fromTimestamp
        ORDER BY m.timestamp DESC
    """)
    fun getFeedMomentsForUser(
        currentUserId: Long,
        fromTimestamp: Long
    ): Flow<List<FeedMomentRow>>
}