package com.example.micelios.data.repository

import com.example.micelios.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(
        id: String,
        name: String,
        email: String,
        bio: String = "",
        photoUri: String? = null
    ): Result<Unit>

    fun getUserById(userId: String): Flow<User?>

    suspend fun getUserByIdOnce(userId: String): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun userExists(userId: String): Boolean

    suspend fun updateUser(
        userId: String,
        name: String,
        bio: String,
        photoUri: String?
    ): Result<Unit>
}