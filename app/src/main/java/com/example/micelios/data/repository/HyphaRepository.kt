package com.example.micelios.data.repository

import com.example.micelios.data.local.dao.HyphaDao
import com.example.micelios.data.local.entity.HyphaEntity
import com.example.micelios.data.local.entity.HyphaMemberEntity
import com.example.micelios.domain.model.Hypha
import com.example.micelios.domain.model.HyphaDetails
import com.example.micelios.domain.model.HyphaMember
import com.example.micelios.domain.model.HyphaRole
import com.example.micelios.domain.model.HyphaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HyphaRepository(
    private val hyphaDao: HyphaDao
) {

    suspend fun createHypha(
        name: String,
        description: String,
        type: HyphaType,
        creatorUserId: Long,
        creatorDisplayName: String,
        members: List<MemberDraft>
    ): Long {
        val hyphaId = hyphaDao.insertHypha(
            HyphaEntity(
                name = name.trim(),
                description = description.trim(),
                type = type.name,
                creatorUserId = creatorUserId
            )
        )

        val hyphaMembers = buildList {
            add(
                HyphaMemberEntity(
                    hyphaId = hyphaId,
                    userId = creatorUserId,
                    displayName = creatorDisplayName,
                    role = HyphaRole.CREATOR.name
                )
            )

            members
                .filter { it.userId != creatorUserId }
                .forEach { member ->
                    add(
                        HyphaMemberEntity(
                            hyphaId = hyphaId,
                            userId = member.userId,
                            displayName = member.displayName,
                            role = HyphaRole.MEMBER.name
                        )
                    )
                }
        }

        hyphaDao.insertMembers(hyphaMembers)
        return hyphaId
    }

    fun getHyphasForUser(userId: Long): Flow<List<Hypha>> {
        return hyphaDao.getHyphasForUser(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getHyphaById(hyphaId: Long): Flow<Hypha?> {
        return hyphaDao.getHyphaById(hyphaId).map { entity ->
            entity?.toDomain()
        }
    }

    fun getMembersByHypha(hyphaId: Long): Flow<List<HyphaMember>> {
        return hyphaDao.getMembersByHypha(hyphaId).map { list ->
            list.map { entity ->
                HyphaMember(
                    id = entity.id,
                    hyphaId = entity.hyphaId,
                    userId = entity.userId,
                    displayName = entity.displayName,
                    role = HyphaRole.valueOf(entity.role),
                    joinedAt = entity.joinedAt
                )
            }
        }
    }

    fun getHyphaDetails(hyphaId: Long): Flow<HyphaDetails?> {
        return hyphaDao.getHyphaWithMembers(hyphaId).map { relation ->
            relation?.let {
                HyphaDetails(
                    hypha = it.hypha.toDomain(),
                    members = it.members.map { member ->
                        HyphaMember(
                            id = member.id,
                            hyphaId = member.hyphaId,
                            userId = member.userId,
                            displayName = member.displayName,
                            role = HyphaRole.valueOf(member.role),
                            joinedAt = member.joinedAt
                        )
                    }
                )
            }
        }
    }

    private fun HyphaEntity.toDomain(): Hypha {
        return Hypha(
            id = id,
            name = name,
            description = description,
            type = HyphaType.valueOf(type),
            creatorUserId = creatorUserId,
            createdAt = createdAt
        )
    }
}

data class MemberDraft(
    val userId: Long,
    val displayName: String
)