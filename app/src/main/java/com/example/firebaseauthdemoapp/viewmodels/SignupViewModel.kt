package com.example.firebaseauthdemoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthdemoapp.repository.UserRepository
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _signupState = MutableStateFlow<SignupState>(SignupState.Idle)
    val signupState: StateFlow<SignupState> get() = _signupState

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    fun signup(email: String, password: String, name: String) {
        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            val error = "All fields must be filled out"
            updateErrorState(error)
            return
        }

        _signupState.value = SignupState.Loading

        viewModelScope.launch {
            userRepository.signup(email, password, name)
                .onSuccess { user ->
                    _signupState.value = SignupState.Success(user)
                    clearError()
                }
                .onFailure { exception ->
                    val error = when (exception) {
                        is FirebaseAuthException -> mapFirebaseAuthError(exception)
                        else -> exception.localizedMessage ?: "An unknown error occurred."
                    }
                    updateErrorState(error)
                }
        }

    }

    private fun mapFirebaseAuthError(e: FirebaseAuthException): String {
        return when (e.errorCode) {
            "ERROR_EMAIL_ALREADY_IN_USE" -> "The email address is already in use."
            "ERROR_WEAK_PASSWORD" -> "The password is too weak. Please use a stronger password."
            "ERROR_INVALID_EMAIL" -> "The email address is invalid. Please enter a valid email."
            "ERROR_USER_DISABLED" -> "The user account has been disabled by an administrator."
            "ERROR_USER_NOT_FOUND" -> "No user found with this email address."
            "ERROR_WRONG_PASSWORD" -> "The password is incorrect. Please try again."
            "ERROR_TOO_MANY_REQUESTS" -> "Too many requests have been made. Please try again later."
            "ERROR_OPERATION_NOT_ALLOWED" -> "The operation is not allowed. Please check your Firebase settings."
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> "An account already exists with the same email address but different sign-in credentials."
            "ERROR_NETWORK_REQUEST_FAILED" -> "Network error occurred. Please check your internet connection."
            else -> "Firebase error: ${e.localizedMessage}"
        }
    }


    private fun updateErrorState(message: String) {
        _signupState.value = SignupState.Failure(message)
        _errorMessage.value = message
    }

    fun clearError() {
        _errorMessage.value = ""
    }

    sealed class SignupState {
        object Idle : SignupState()
        object Loading : SignupState()
        data class Success(val user: FirebaseUser) : SignupState()
        data class Failure(val errorMessage: String) : SignupState()
    }
}
