package com.example.micelios.di

import com.example.micelios.data.remote.auth.FirebaseAuthRepository
import com.example.micelios.data.remote.firestore.FirebaseHyphaRepository
import com.example.micelios.data.remote.firestore.FirebaseMessageRepository
import com.example.micelios.data.remote.firestore.FirebaseMomentRepository
import com.example.micelios.data.remote.firestore.FirebaseUserRepository
import com.example.micelios.data.repository.AuthRepository
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MessageRepository
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.data.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: FirebaseAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: FirebaseUserRepository
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindHyphaRepository(
        impl: FirebaseHyphaRepository
    ): HyphaRepository

    @Binds
    @Singleton
    abstract fun bindMomentRepository(
        impl: FirebaseMomentRepository
    ): MomentRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(
        impl: FirebaseMessageRepository
    ): MessageRepository
}