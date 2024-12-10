package com.example.firebaseauthdemoapp.repository

import com.example.firebaseauthdemoapp.network.FirebaseAuthSource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

interface UserRepository {
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun signup(email: String, password: String, name: String): Result<FirebaseUser>
    suspend fun logout(): Result<Unit>
    fun getCurrentUser(): FirebaseUser?
}


class UserRepositoryImpl @Inject constructor(
    private val firebaseAuthSource: FirebaseAuthSource
) : UserRepository {

    override suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return runCatching {
            val authResult = firebaseAuthSource.login(email, password)
            authResult.user ?: throw Exception("User is null after login.")
        }
    }

    override suspend fun signup(email: String, password: String, name: String): Result<FirebaseUser> {
        return runCatching {
            val authResult = firebaseAuthSource.signup(email, password, name)
            authResult.user ?: throw Exception("User is null after signup.")
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching {
            firebaseAuthSource.logout()
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuthSource.getCurrentUser()
    }
}
