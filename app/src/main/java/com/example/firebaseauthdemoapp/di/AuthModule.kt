package com.example.firebaseauthdemoapp.di

import com.example.firebaseauthdemoapp.network.FirebaseAuthSource
import com.example.firebaseauthdemoapp.repository.UserRepository
import com.example.firebaseauthdemoapp.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun provideFirebaseAuthSource(firebaseAuth: FirebaseAuth): FirebaseAuthSource {
        return FirebaseAuthSource(firebaseAuth)
    }


    @Provides
    @Singleton
    fun provideUserRepository(firebaseAuthSource: FirebaseAuthSource): UserRepository {
        return UserRepositoryImpl(firebaseAuthSource)
    }
}
