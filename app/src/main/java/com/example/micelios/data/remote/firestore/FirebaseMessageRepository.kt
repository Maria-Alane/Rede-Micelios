package com.example.micelios.data.remote.firestore

import com.example.micelios.data.mapper.toDomain
import com.example.micelios.data.remote.dto.MessageDocument
import com.example.micelios.data.repository.MessageRepository
import com.example.micelios.domain.model.Message
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseMessageRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : MessageRepository {

    override fun getMessagesByHypha(hyphaId: String): Flow<List<Message>> = callbackFlow {
        val registration = firestore.collection("messages")
            .whereEqualTo("hyphaId", hyphaId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents
                    ?.mapNotNull { it.toObject(MessageDocument::class.java)?.toDomain() }
                    ?.sortedBy { it.timestamp }
                    .orEmpty()

                trySend(messages)
            }

        awaitClose { registration.remove() }
    }

    override suspend fun insertMessage(
        hyphaId: String,
        senderUserId: String,
        content: String
    ): Result<String> {
        return try {
            val memberSnapshot = firestore.collection("hyphaMembers")
                .whereEqualTo("hyphaId", hyphaId)
                .whereEqualTo("userId", senderUserId)
                .get()
                .await()

            val senderDisplayName = memberSnapshot.documents
                .firstOrNull()
                ?.getString("displayName")
                .orEmpty()

            val ref = firestore.collection("messages").document()

            ref.set(
                mapOf(
                    "id" to ref.id,
                    "hyphaId" to hyphaId,
                    "senderUserId" to senderUserId,
                    "senderDisplayName" to senderDisplayName,
                    "content" to content.trim(),
                    "timestamp" to FieldValue.serverTimestamp()
                )
            ).await()

            Result.success(ref.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}