package com.example.micelios.data.remote.firestore

import com.example.micelios.data.mapper.toDomain
import com.example.micelios.data.remote.dto.UserDocument
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.domain.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun createUser(
        id: String,
        name: String,
        email: String,
        bio: String,
        photoUri: String?
    ): Result<Unit> {
        return try {
            val trimmedEmail = email.trim()
            firestore.collection("users")
                .document(id)
                .set(
                    mapOf(
                        "id" to id,
                        "name" to name.trim(),
                        "email" to trimmedEmail,
                        "emailLowercase" to trimmedEmail.lowercase(),
                        "bio" to bio,
                        "photoUri" to photoUri,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUserById(userId: String): Flow<User?> = callbackFlow {
        val registration = firestore.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val user = snapshot?.toObject(UserDocument::class.java)?.toDomain()
                trySend(user)
            }

        awaitClose { registration.remove() }
    }

    override suspend fun getUserByIdOnce(userId: String): User? {
        return try {
            firestore.collection("users")
                .document(userId)
                .get()
                .await()
                .toObject(UserDocument::class.java)
                ?.toDomain()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return try {
            firestore.collection("users")
                .whereEqualTo("emailLowercase", email.trim().lowercase())
                .limit(1)
                .get()
                .await()
                .documents
                .firstOrNull()
                ?.toObject(UserDocument::class.java)
                ?.toDomain()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun userExists(userId: String): Boolean {
        return try {
            firestore.collection("users")
                .document(userId)
                .get()
                .await()
                .exists()
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun updateUser(
        userId: String,
        name: String,
        bio: String,
        photoUri: String?
    ): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update(
                    mapOf(
                        "name" to name.trim(),
                        "bio" to bio,
                        "photoUri" to photoUri
                    )
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}