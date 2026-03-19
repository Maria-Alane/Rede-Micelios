package com.example.micelios.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hypha_table")
data class HyphaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val description: String = "",
    val type: String,
    val creatorUserId: Long,
    val createdAt: Long = System.currentTimeMillis()
)