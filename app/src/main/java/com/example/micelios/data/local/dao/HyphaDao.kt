package com.example.micelios.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.micelios.data.local.entity.HyphaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HyphaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hypha: HyphaEntity)

    @Query("SELECT * FROM hypha_table ORDER BY createdAt DESC")
    fun getAllHyphas(): Flow<List<HyphaEntity>>

    @Query("SELECT * FROM hypha_table WHERE id = :hyphaId LIMIT 1")
    fun getHyphaById(hyphaId: Long): Flow<HyphaEntity?>
}