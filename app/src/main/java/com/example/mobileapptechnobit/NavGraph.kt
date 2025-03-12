package com.example.mobileapptechnobit

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobileapptechnobit.ui.*
import com.example.mobileapptechnobit.ViewModel.LoginViewModel

@Composable
fun NavGraph(navController: NavHostController, loginViewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val token = remember { sharedPref.getString("AUTH_TOKEN", null) }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Walkthrough.route) {
            WalkthroughScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                navCtrl = navController
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    loginViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.forgotPassword.route) {
            HalamanLupaPassword(navCtrl = navController)
        }
        composable(Screen.ResetPassword.route) { backStackEntry ->
            HalamanResetPassword(
                navCtrl = navController,
                email = backStackEntry.arguments?.getString("email") ?: ""
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController, token = token ?: "")
        }
        composable(Screen.DetailProfile.route) {
            DetailProfileScreen(navController = navController, token = token ?: "")
        }
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Walkthrough : Screen("walkthrough_screen")
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object forgotPassword : Screen("forgot_password_screen")
    object ResetPassword : Screen("reset_password_screen/{email}")
    object Profile : Screen("profile_screen")
    object DetailProfile : Screen("detailprofile_screen")
}