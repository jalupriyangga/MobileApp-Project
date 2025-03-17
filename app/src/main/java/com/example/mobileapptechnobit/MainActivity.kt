package com.example.mobileapptechnobit

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.ViewModel.AuthViewModel
import com.example.mobileapptechnobit.ViewModel.AuthViewModelFactory
import com.example.mobileapptechnobit.data.API.ApiClient
import com.example.mobileapptechnobit.data.repository.AuthRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiClient.init(this)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val context = this
                    val authRepository = AuthRepository()
                    val authViewModel: AuthViewModel = viewModel(
                        factory = AuthViewModelFactory(authRepository, context)
                    )

                    LaunchedEffect(Unit) {
                        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val token = sharedPref.getString("AUTH_TOKEN", null)

                        if (token != null) {
                            navController.navigate("home_screen") {
                                popUpTo("login_screen") { inclusive = true }
                            }
                        }
                    }
                    NavGraph(navController = navController, authViewModel = authViewModel)
                }
            }
        }
    }
}