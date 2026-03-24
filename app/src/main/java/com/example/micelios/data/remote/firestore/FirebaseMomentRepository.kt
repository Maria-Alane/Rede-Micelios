package com.example.micelios.data.remote.firestore

import com.example.micelios.data.mapper.toFeedDomain
import com.example.micelios.data.mapper.toMomentDomain
import com.example.micelios.data.remote.dto.MomentDocument
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.domain.model.FeedMoment
import com.example.micelios.domain.model.Moment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseMomentRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : MomentRepository {

    override fun getMomentsByHypha(hyphaId: String): Flow<List<Moment>> = callbackFlow {
        val registration = firestore.collection("moments")
            .whereEqualTo("hyphaId", hyphaId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val moments = snapshot?.documents
                    ?.mapNotNull { it.toObject(MomentDocument::class.java)?.toMomentDomain() }
                    ?.sortedByDescending { it.timestamp }
                    .orEmpty()

                trySend(moments)
            }

        awaitClose { registration.remove() }
    }

    override fun getFeedMomentsForUser(userId: String): Flow<List<FeedMoment>> = callbackFlow {
        val registration = firestore.collection("moments")
            .whereArrayContains("visibleToUserIds", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val fromTimestampMillis = System.currentTimeMillis() - (24 * 60 * 60 * 1000L)

                val feed = snapshot?.documents
                    ?.mapNotNull { it.toObject(MomentDocument::class.java)?.toFeedDomain() }
                    ?.filter { it.timestamp >= fromTimestampMillis }
                    ?.sortedByDescending { it.timestamp }
                    .orEmpty()

                trySend(feed)
            }

        awaitClose { registration.remove() }
    }

    override suspend fun insertMoment(
        hyphaId: String,
        creatorUserId: String,
        content: String,
        photoUri: String?
    ): Result<String> {
        return try {
            val hyphaSnapshot = firestore.collection("hyphas")
                .document(hyphaId)
                .get()
                .await()

            val hyphaName = hyphaSnapshot.getString("name").orEmpty()

            val memberSnapshot = firestore.collection("hyphaMembers")
                .whereEqualTo("hyphaId", hyphaId)
                .get()
                .await()

            val creatorDisplayName = memberSnapshot.documents
                .firstOrNull { it.getString("userId") == creatorUserId }
                ?.getString("displayName")
                .orEmpty()

            val visibleToUserIds = memberSnapshot.documents
                .mapNotNull { it.getString("userId") }

            val userSnapshot = firestore.collection("users")
                .document(creatorUserId)
                .get()
                .await()

            val creatorPhotoUri = userSnapshot.getString("photoUri")

            val ref = firestore.collection("moments").document()

            ref.set(
                mapOf(
                    "id" to ref.id,
                    "hyphaId" to hyphaId,
                    "hyphaName" to hyphaName,
                    "creatorUserId" to creatorUserId,
                    "creatorDisplayName" to creatorDisplayName,
                    "creatorPhotoUri" to creatorPhotoUri,
                    "visibleToUserIds" to visibleToUserIds,
                    "content" to content.trim(),
                    "photoUri" to photoUri,
                    "timestamp" to FieldValue.serverTimestamp()
                )
            ).await()

            Result.success(ref.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}