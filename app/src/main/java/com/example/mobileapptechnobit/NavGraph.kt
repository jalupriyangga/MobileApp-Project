package com.example.mobileapptechnobit

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobileapptechnobit.ui.*
import com.example.mobileapptechnobit.ViewModel.LoginViewModel
import com.example.mobileapptechnobit.ui.ForgotPasswordScreen
import com.example.mobileapptechnobit.ui.ResetPasswordScreen
import com.example.mobileapptechnobit.ui.component.SuccessScreen

@Composable
fun NavGraph(navController: NavHostController, loginViewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    NavHost(
        navController = navController,
        startDestination = if (loginViewModel.isLoggedIn()) Screen.Home.route else Screen.Splash.route
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
        composable(Screen.ForgotPassword.route){
            ForgotPasswordScreen(navCtrl = navController)
        }
        composable(Screen.ResetPassword.route){ backStackEntry ->
            ResetPasswordScreen(
                navCtrl = navController,
                email = backStackEntry.arguments?.getString("email") ?: ""
            )
        }
//        composable(Screen.Home.route) {
//            HomeScreen(
//                onLogout = {
//                    loginViewModel.logout()
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(Screen.Home.route) { inclusive = true }
//                    }
//                }
//            )
//        }
        composable(Screen.Home.route){
            com.example.mobileapptechnobit.ui.HomeScreen(navCtrl = navController)
        }
        composable(Screen.Schedule.route){
            ScheduleScreen(navCtrl = navController)
        }
        composable(Screen.Success.route){ backStackEntry ->
            SuccessScreen(
                navCtrl =  navController,
                message = backStackEntry.arguments?.getString("message") ?: ""
            )
        }
        composable(Screen.History.route){
            HistoryScreen(navCtrl = navController)
        }
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Walkthrough : Screen("walkthrough_screen")
    object Login : Screen("login_screen")
    object  ForgotPassword : Screen("forgot_password_screen")
    object ResetPassword : Screen("reset_password_screen/{email}")
    object Success : Screen("success_screen/{message}")
    object Home : Screen("home_screen")
    object Schedule: Screen("schedule_screen")
    object History : Screen("history_screen")
    object Profile : Screen("profile_screen")
}