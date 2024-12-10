package com.example.firebaseauthdemoapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebaseauthdemoapp.viewmodel.LoginViewModel
import com.example.firebaseauthdemoapp.components.CustomButton
import com.example.firebaseauthdemoapp.components.CustomTextField
import com.google.firebase.auth.FirebaseUser

@Composable
fun Login(
    onLoginSuccess: (user: FirebaseUser) -> Unit,
    onNavigateToSignup: () -> Unit
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState = loginViewModel.loginState.collectAsState().value
    val errorMessage = loginViewModel.errorMessage.collectAsState().value // Collect the error message

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Login", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(32.dp))

        // Email Field
        CustomTextField(
            value = email.value,
            onValueChange = {
                email.value = it
                loginViewModel.clearError()
            },
            label = "Email"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        CustomTextField(
            value = password.value,
            onValueChange = {
                password.value = it
                loginViewModel.clearError()
            },
            label = "Password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))


        when (loginState) {
            is LoginViewModel.LoginState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
            }
            else -> {
                CustomButton(label = "Login") {
                    loginViewModel.login(email.value, password.value)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        TextButton(onClick = { onNavigateToSignup() }) {
            Text("Don't have an account? Sign up")
        }

        // Handle the Login success case
        if (loginState is LoginViewModel.LoginState.Success) {
            LaunchedEffect(Unit) {
                loginState.user.let { onLoginSuccess(it) }
            }
        }
    }
}
