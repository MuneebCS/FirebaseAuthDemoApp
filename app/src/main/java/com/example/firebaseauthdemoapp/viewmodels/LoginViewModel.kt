package com.example.firebaseauthdemoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthdemoapp.repository.UserRepository
import com.example.firebaseauthdemoapp.viewmodel.SignupViewModel.SignupState
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    fun login(email: String, password: String) {
        if(email.isBlank() || password.isBlank()){
            val error = "All fields must be filled out"
            updateErrorState(error)
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
          userRepository.login(email, password)
              .onSuccess {
                  user ->
                  _loginState.value = LoginState.Success(user)
                  clearError()
              }
              .onFailure {
                 exception -> val error = when(exception){
                  is FirebaseAuthException -> mapFirebaseAuthError(exception)
                  else -> exception.localizedMessage ?: "An unknown error occurred."
                 }
                  updateErrorState(error)
              }
        }
    }

    private fun mapFirebaseAuthError(e: FirebaseAuthException): String {
        return when (e.errorCode) {

            "ERROR_INVALID_CREDENTIAL" -> "Invalid Credentials. Please try again."
            "ERROR_USER_NOT_FOUND" -> "No user found with this email address."
            "ERROR_INVALID_EMAIL" -> "The email address is invalid. Please enter a valid email."
            "ERROR_TOO_MANY_REQUESTS" -> "Too many requests have been made. Please try again later."
            "ERROR_OPERATION_NOT_ALLOWED" -> "The operation is not allowed. Please check your Firebase settings."
            "ERROR_NETWORK_REQUEST_FAILED" -> "Network error occurred. Please check your internet connection."
            else -> "Firebase error: ${e.localizedMessage}"
        }
    }


    fun clearError() {
        _errorMessage.value = ""
    }

    private fun updateErrorState(message: String) {
        _loginState.value = LoginState.Failure(message)
        _errorMessage.value = message
    }


    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val user: FirebaseUser) : LoginState()
        data class Failure(val errorMessage: String) : LoginState()
    }
}

