package com.example.micelios.data.repository

import com.example.micelios.domain.model.Hypha
import com.example.micelios.domain.model.HyphaDetails
import com.example.micelios.domain.model.HyphaMember
import com.example.micelios.domain.model.HyphaType
import kotlinx.coroutines.flow.Flow

interface HyphaRepository {

    suspend fun createHypha(
        name: String,
        description: String,
        type: HyphaType,
        creatorUserId: String,
        creatorDisplayName: String,
        members: List<MemberDraft>
    ): Result<String>

    fun getHyphasForUser(userId: String): Flow<List<Hypha>>

    fun getHyphaById(hyphaId: String): Flow<Hypha?>

    fun getMembersByHypha(hyphaId: String): Flow<List<HyphaMember>>

    fun getHyphaDetails(hyphaId: String): Flow<HyphaDetails?>
}

data class MemberDraft(
    val userId: String,
    val displayName: String
)