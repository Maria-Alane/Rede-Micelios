package com.example.micelios.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val bio: String = "",
    val photoUri: String? = null,
    val memberSince: Long = System.currentTimeMillis()
)