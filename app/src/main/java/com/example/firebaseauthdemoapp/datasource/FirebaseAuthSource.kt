package com.example.firebaseauthdemoapp.network
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,

) {

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        } catch (e: FirebaseAuthException) {
            throw (e)
        }
    }

    suspend fun signup(email: String, password: String, name: String): AuthResult {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            )?.await()
            authResult
        } catch (e: FirebaseAuthException) {
            throw (e)
        }
    }

    fun logout() {
        try{
            firebaseAuth.signOut()
        }
        catch (e: FirebaseAuthException) {
            throw (e)
        }

    }

    fun getCurrentUser(): FirebaseUser? {

        try{
            return firebaseAuth.currentUser
        }
        catch (e: FirebaseAuthException) {
            throw (e)
        }
    }
}
