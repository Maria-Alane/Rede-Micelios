package com.example.micelios.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "hypha_member_table",
    foreignKeys = [
        ForeignKey(
            entity = HyphaEntity::class,
            parentColumns = ["id"],
            childColumns = ["hyphaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["hyphaId"]),
        Index(value = ["userId"]),
        Index(value = ["hyphaId", "userId"], unique = true)
    ]
)
data class HyphaMemberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val hyphaId: Long,
    val userId: Long,
    val displayName: String,
    val role: String,
    val joinedAt: Long = System.currentTimeMillis()
)