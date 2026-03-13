package com.example.micelios.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.micelios.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert
    suspend fun insert(message: MessageEntity)

    @Query("""
        SELECT * FROM message_table
        WHERE hyphaId = :hyphaId
        ORDER BY timestamp ASC
    """)
    fun getMessagesByHypha(hyphaId: Long): Flow<List<MessageEntity>>
}