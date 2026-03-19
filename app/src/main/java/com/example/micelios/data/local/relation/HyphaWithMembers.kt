package com.example.micelios.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.micelios.data.local.entity.HyphaEntity
import com.example.micelios.data.local.entity.HyphaMemberEntity

data class HyphaWithMembers(
    @Embedded
    val hypha: HyphaEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "hyphaId"
    )
    val members: List<HyphaMemberEntity>
)