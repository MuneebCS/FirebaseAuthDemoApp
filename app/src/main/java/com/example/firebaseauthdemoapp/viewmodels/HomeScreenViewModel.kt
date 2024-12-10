package com.example.firebaseauthdemoapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthdemoapp.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState.Idle)
    val userState: StateFlow<UserState> get() = _userState

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        val currentUser = userRepository.getCurrentUser()
        _userState.value = if (currentUser != null) {
            UserState.LoggedIn(currentUser)
        } else {
            UserState.LoggedOut
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _userState.value = UserState.LoggedOut
        }
    }

    sealed class UserState {
        object Idle : UserState()
        data class LoggedIn(val user: FirebaseUser) : UserState()
        object LoggedOut : UserState()
    }
}
