package com.example.firebaseauthdemoapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebaseauthdemoapp.viewmodels.HomeScreenViewModel

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {

    val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()

    val userState = homeScreenViewModel.userState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (userState) {
            is HomeScreenViewModel.UserState.LoggedIn -> {
                Text("Welcome, ${userState.user.displayName ?: "User"}", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Email: ${userState.user.email}", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = onLogout) {
                    Text("Logout")
                }
            }
            HomeScreenViewModel.UserState.LoggedOut -> {
                Text("You are logged out. Please log in.", style = MaterialTheme.typography.headlineLarge)
            }
            HomeScreenViewModel.UserState.Idle -> {
                CircularProgressIndicator()
            }
        }
    }
}
