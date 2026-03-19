package com.example.micelios.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.micelios.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM user_table WHERE id = :userId LIMIT 1")
    fun getUserById(userId: Long): Flow<UserEntity?>

    @Query("SELECT * FROM user_table WHERE id = :userId LIMIT 1")
    suspend fun getUserByIdOnce(userId: Long): UserEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM user_table WHERE id = :userId)")
    suspend fun existsById(userId: Long): Boolean
}