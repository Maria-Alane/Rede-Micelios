package com.example.micelios.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.micelios.data.local.entity.MessageEntity
import com.example.micelios.data.local.relation.ChatMessageRow
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert
    suspend fun insert(message: MessageEntity)

    @Query("""
        SELECT
            msg.id AS id,
            msg.hyphaId AS hyphaId,
            msg.senderUserId AS senderUserId,
            hm.displayName AS senderDisplayName,
            msg.content AS content,
            msg.timestamp AS timestamp
        FROM message_table msg
        INNER JOIN hypha_member_table hm
            ON hm.hyphaId = msg.hyphaId
           AND hm.userId = msg.senderUserId
        WHERE msg.hyphaId = :hyphaId
        ORDER BY msg.timestamp ASC
    """)
    fun getMessagesByHypha(hyphaId: Long): Flow<List<ChatMessageRow>>
}