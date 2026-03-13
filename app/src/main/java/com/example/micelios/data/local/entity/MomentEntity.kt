package com.example.micelios.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moment_table")
data class MomentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hyphaId: Long,
    val creatorName: String,
    val content: String,
    val photoUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)