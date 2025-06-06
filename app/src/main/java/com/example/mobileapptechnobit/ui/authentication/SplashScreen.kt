package com.example.mobileapptechnobit.ui.authentication

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobileapptechnobit.R
import com.example.mobileapptechnobit.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startExitAnimation by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (startExitAnimation) 0f else 1f,
        animationSpec = tween(200), label = "alpha"
    )
    val scale by animateFloatAsState(
        targetValue = if (startExitAnimation) 0.8f else 1f,
        animationSpec = tween(200), label = "scale"
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
            .background(Color(0xFF2752E7)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_siputih),
            contentDescription = "Logo Sijaga",
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
                .size(100.dp)
        )
    }
}
