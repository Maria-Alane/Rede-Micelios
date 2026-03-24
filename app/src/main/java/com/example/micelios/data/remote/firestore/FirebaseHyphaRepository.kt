package com.example.micelios.data.remote.firestore

import com.example.micelios.data.mapper.toDomain
import com.example.micelios.data.remote.dto.HyphaDocument
import com.example.micelios.data.remote.dto.HyphaMemberDocument
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MemberDraft
import com.example.micelios.domain.model.Hypha
import com.example.micelios.domain.model.HyphaDetails
import com.example.micelios.domain.model.HyphaMember
import com.example.micelios.domain.model.HyphaRole
import com.example.micelios.domain.model.HyphaType
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseHyphaRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : HyphaRepository {

    override suspend fun createHypha(
        name: String,
        description: String,
        type: HyphaType,
        creatorUserId: String,
        creatorDisplayName: String,
        members: List<MemberDraft>
    ): Result<String> {
        return try {
            val hyphaRef = firestore.collection("hyphas").document()
            val hyphaId = hyphaRef.id

            val allMembers = buildList {
                add(MemberDraft(creatorUserId, creatorDisplayName))
                addAll(members.filter { it.userId != creatorUserId })
            }.distinctBy { it.userId }

            val batch = firestore.batch()

            batch.set(
                hyphaRef,
                mapOf(
                    "id" to hyphaId,
                    "name" to name.trim(),
                    "description" to description.trim(),
                    "type" to type.name,
                    "creatorUserId" to creatorUserId,
                    "memberUserIds" to allMembers.map { it.userId },
                    "createdAt" to FieldValue.serverTimestamp()
                )
            )

            allMembers.forEachIndexed { index, member ->
                val memberRef = firestore.collection("hyphaMembers").document()
                batch.set(
                    memberRef,
                    mapOf(
                        "id" to memberRef.id,
                        "hyphaId" to hyphaId,
                        "userId" to member.userId,
                        "displayName" to member.displayName,
                        "role" to if (index == 0) HyphaRole.CREATOR.name else HyphaRole.MEMBER.name,
                        "joinedAt" to FieldValue.serverTimestamp()
                    )
                )
            }

            batch.commit().await()
            Result.success(hyphaId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getHyphasForUser(userId: String): Flow<List<Hypha>> = callbackFlow {
        val registrations = mutableListOf<com.google.firebase.firestore.ListenerRegistration>()

        val membershipRegistration = firestore.collection("hyphaMembers")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { membershipSnapshot, membershipError ->
                if (membershipError != null) {
                    close(membershipError)
                    return@addSnapshotListener
                }

                val hyphaIds = membershipSnapshot?.documents
                    ?.mapNotNull { it.getString("hyphaId") }
                    ?.distinct()
                    .orEmpty()

                registrations.drop(1).forEach { it.remove() }
                while (registrations.size > 1) {
                    registrations.removeLast()
                }

                if (hyphaIds.isEmpty()) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val results = mutableMapOf<String, Hypha>()

                hyphaIds.chunked(10).forEach { chunk ->
                    val registration = firestore.collection("hyphas")
                        .whereIn("id", chunk)
                        .addSnapshotListener { snapshot, error ->
                            if (error != null) {
                                close(error)
                                return@addSnapshotListener
                            }

                            snapshot?.documents
                                ?.mapNotNull { it.toObject(HyphaDocument::class.java)?.toDomain() }
                                .orEmpty()
                                .forEach { results[it.id] = it }

                            trySend(results.values.sortedByDescending { it.createdAt })
                        }

                    registrations.add(registration)
                }
            }

        registrations.add(membershipRegistration)

        awaitClose { registrations.forEach { it.remove() } }
    }

    override fun getHyphaById(hyphaId: String): Flow<Hypha?> = callbackFlow {
        val registration = firestore.collection("hyphas")
            .document(hyphaId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                trySend(snapshot?.toObject(HyphaDocument::class.java)?.toDomain())
            }

        awaitClose { registration.remove() }
    }

    override fun getMembersByHypha(hyphaId: String): Flow<List<HyphaMember>> = callbackFlow {
        val registration = firestore.collection("hyphaMembers")
            .whereEqualTo("hyphaId", hyphaId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val members = snapshot?.documents
                    ?.mapNotNull { it.toObject(HyphaMemberDocument::class.java)?.toDomain() }
                    ?.sortedBy { it.joinedAt }
                    .orEmpty()

                trySend(members)
            }

        awaitClose { registration.remove() }
    }

    override fun getHyphaDetails(hyphaId: String): Flow<HyphaDetails?> = callbackFlow {
        var currentHypha: Hypha? = null
        var currentMembers: List<HyphaMember> = emptyList()

        val hyphaRegistration = firestore.collection("hyphas")
            .document(hyphaId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                currentHypha = snapshot?.toObject(HyphaDocument::class.java)?.toDomain()
                trySend(currentHypha?.let { HyphaDetails(it, currentMembers) })
            }

        val membersRegistration = firestore.collection("hyphaMembers")
            .whereEqualTo("hyphaId", hyphaId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                currentMembers = snapshot?.documents
                    ?.mapNotNull { it.toObject(HyphaMemberDocument::class.java)?.toDomain() }
                    ?.sortedBy { it.joinedAt }
                    .orEmpty()

                trySend(currentHypha?.let { HyphaDetails(it, currentMembers) })
            }

        awaitClose {
            hyphaRegistration.remove()
            membersRegistration.remove()
        }
    }
}