package com.example.micelios.data.repository

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<String>
    suspend fun signIn(email: String, password: String): Result<String>
    fun getCurrentUserId(): String?
    fun isLoggedIn(): Boolean
    fun signOut()
}