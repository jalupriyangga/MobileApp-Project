package com.example.mobileapptechnobit.ui.authentication

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileapptechnobit.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startExitAnimation by remember { mutableStateOf(false) }


    val alpha by animateFloatAsState(
        targetValue = if (startExitAnimation) 0f else 1f,
        animationSpec = tween(200)
    )
    val scale by animateFloatAsState(
        targetValue = if (startExitAnimation) 0.8f else 1f,
        animationSpec = tween(200)
    )

    LaunchedEffect(Unit) {
        delay(1200)
        startExitAnimation = true
        delay(10)

        navController.navigate(Screen.Walkthrough.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2752E7)), // Warna biru sesuai gambar
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Arunika",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
        )
    }
}