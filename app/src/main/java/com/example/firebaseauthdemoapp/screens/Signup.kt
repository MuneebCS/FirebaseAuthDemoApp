package com.example.firebaseauthdemoapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebaseauthdemoapp.components.CustomButton
import com.example.firebaseauthdemoapp.components.CustomTextField
import com.example.firebaseauthdemoapp.viewmodel.LoginViewModel
import com.example.firebaseauthdemoapp.viewmodel.SignupViewModel
import com.google.firebase.auth.FirebaseUser

@Composable
fun Signup(
    onSignupSuccess: (user: FirebaseUser) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }

    val signupViewModel: SignupViewModel = hiltViewModel()
    val signupState = signupViewModel.signupState.collectAsState().value
    val errorMessage = signupViewModel.errorMessage.collectAsState().value

    // Reset error state on text field change
    fun clearErrors() {
        signupViewModel.clearError()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Sign Up", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(32.dp))

        CustomTextField(
            value = name.value,
            onValueChange = {
                name.value = it
                clearErrors()
            },
            label = "Full Name"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        CustomTextField(
            value = email.value,
            onValueChange = {
                email.value = it
                clearErrors()
            },
            label = "Email"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        CustomTextField(
            value = password.value,
            onValueChange = {
                password.value = it
                clearErrors()
            },
            label = "Password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp)) 


        when (signupState) {
            is SignupViewModel.SignupState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
            }
            else -> {
                CustomButton(label = "Sign Up") {
                    signupViewModel.signup(email.value, password.value, name.value)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) 

        // Error message
        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) 

        // Already have an account?
        TextButton(onClick = { onNavigateToLogin() }) {
            Text("Already have an account? Log in")
        }
    }

    // Navigate to success screen once sign up is complete
    if (signupState is SignupViewModel.SignupState.Success) {
        LaunchedEffect(Unit) {
            signupState.user.let { onSignupSuccess(it) }
        }
    }
}
