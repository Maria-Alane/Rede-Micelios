package com.example.micelios.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_table")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hyphaId: Long,
    val senderName: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)