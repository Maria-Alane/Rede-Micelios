package com.example.micelios.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.micelios.data.local.entity.ReactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReactionDao {

    @Insert
    suspend fun insert(reaction: ReactionEntity)

    @Query("SELECT * FROM reaction_table WHERE momentId = :momentId")
    fun getReactionsByMoment(momentId: Long): Flow<List<ReactionEntity>>
}