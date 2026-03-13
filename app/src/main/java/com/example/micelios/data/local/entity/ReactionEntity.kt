package com.example.micelios.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reaction_table")
data class ReactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val momentId: Long,
    val userName: String,
    val type: String
)