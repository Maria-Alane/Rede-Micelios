package com.example.micelios.data.repository
//
//import com.example.micelios.data.local.dao.UserDao
//import com.example.micelios.data.local.entity.UserEntity
//import com.example.micelios.domain.model.User
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//class LocalUserRepository(
//    private val userDao: UserDao
//) : UserRepository {
//
//    override suspend fun createUser(
//        id: String,
//        name: String,
//        bio: String,
//        photoUri: String?
//    ): Result<Unit> {
//        return try {
//            userDao.insert(
//                UserEntity(
//                    id = id,
//                    name = name.trim(),
//                    bio = bio,
//                    photoUri = photoUri
//                )
//            )
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    override fun getUserById(userId: String): Flow<User?> {
//        return userDao.getUserById(userId).map { it?.toDomain() }
//    }
//
//    override suspend fun getUserByIdOnce(userId: String): User? {
//        return userDao.getUserByIdOnce(userId)?.toDomain()
//    }
//
//    override suspend fun userExists(userId: String): Boolean {
//        return userDao.existsById(userId)
//    }
//
//    override suspend fun updateUser(
//        userId: String,
//        name: String,
//        bio: String,
//        photoUri: String?
//    ): Result<Unit> {
//        return try {
//            userDao.update(
//                UserEntity(
//                    id = userId,
//                    name = name,
//                    bio = bio,
//                    photoUri = photoUri
//                )
//            )
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    private fun UserEntity.toDomain(): User {
//        return User(
//            id = id,
//            name = name,
//            bio = bio,
//            photoUri = photoUri,
//            memberSince = memberSince
//        )
//    }
//}