package com.example.micelios.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.micelios.data.local.entity.MomentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MomentDao {

    @Insert
    suspend fun insert(moment: MomentEntity)

    @Query("""
        SELECT * FROM moment_table
        WHERE hyphaId = :hyphaId
        ORDER BY timestamp DESC
    """)
    fun getMomentsByHypha(hyphaId: Long): Flow<List<MomentEntity>>

    @Query("""
        SELECT * FROM moment_table
        WHERE timestamp >= :fromTimestamp
        ORDER BY timestamp DESC
    """)
    fun getFeedMoments(fromTimestamp: Long): Flow<List<MomentEntity>>
}