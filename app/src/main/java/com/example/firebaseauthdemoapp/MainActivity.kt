package com.example.firebaseauthdemoapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.firebaseauthdemoapp.screens.Login
import com.example.firebaseauthdemoapp.ui.AppNavGraph
import com.example.firebaseauthdemoapp.ui.theme.FirebaseAuthDemoAppTheme
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseAuthDemoAppTheme {
                AppNavGraph()
            }
        }
    }
}
