package com.example.micelios.data.repository

import com.example.micelios.data.local.dao.UserDao
import com.example.micelios.data.local.entity.UserEntity
import com.example.micelios.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val userDao: UserDao
) {

    suspend fun createUser(
        name: String,
        bio: String = "",
        photoUri: String? = null
    ): Long {
        return userDao.insert(
            UserEntity(
                name = name.trim(),
                bio = bio,
                photoUri = photoUri
            )
        )
    }

    fun getUserById(userId: Long): Flow<User?> {
        return userDao.getUserById(userId).map { entity ->
            entity?.toDomain()
        }
    }

    suspend fun getUserByIdOnce(userId: Long): User? {
        return userDao.getUserByIdOnce(userId)?.toDomain()
    }

    suspend fun userExists(userId: Long): Boolean {
        return userDao.existsById(userId)
    }

    private fun UserEntity.toDomain(): User {
        return User(
            id = id,
            name = name,
            bio = bio,
            photoUri = photoUri,
            memberSince = memberSince
        )
    }
}