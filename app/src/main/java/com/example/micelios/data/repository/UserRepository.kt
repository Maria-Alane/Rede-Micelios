package com.example.micelios.data.repository

import com.example.micelios.data.local.dao.UserDao
import com.example.micelios.data.local.entity.UserEntity
import com.example.micelios.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val userDao: UserDao) {

    fun getUser(): Flow<User?> {
        return userDao.getUser().map { entity ->
            entity?.let {
                User(
                    id = it.id,
                    name = it.name,
                    bio = it.bio,
                    photoUri = it.photoUri,
                    memberSince = it.memberSince
                )
            }
        }
    }

    suspend fun saveUser(name: String, bio: String, photoUri: String?) {
        userDao.insert(
            UserEntity(
                id = 1L,
                name = name,
                bio = bio,
                photoUri = photoUri
            )
        )
    }
}