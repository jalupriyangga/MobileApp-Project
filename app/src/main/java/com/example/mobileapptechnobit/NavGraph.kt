package com.example.mobileapptechnobit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.mobileapptechnobit.ui.DetailScheduleScreen
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobileapptechnobit.ui.*
import com.example.mobileapptechnobit.ui.ForgotPasswordScreen
import com.example.mobileapptechnobit.ui.ResetPasswordScreen
import com.example.mobileapptechnobit.ViewModel.AuthViewModel
import com.example.mobileapptechnobit.ViewModel.AuthViewModelFactory
import com.example.mobileapptechnobit.ViewModel.CameraPresViewModel
import com.example.mobileapptechnobit.ViewModel.PermissionViewModel
import com.example.mobileapptechnobit.ViewModel.PermissionViewModelFactory
import com.example.mobileapptechnobit.data.remote.PermissionResponseItem
import com.example.mobileapptechnobit.ViewModel.PatroliViewModel
import com.example.mobileapptechnobit.data.repository.AuthRepository
import com.example.mobileapptechnobit.data.repository.PermissionRepository
import com.example.mobileapptechnobit.ui.component.SuccessScreen
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("RememberReturnType")
@Composable
fun NavGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val token = remember { sharedPref.getString("AUTH_TOKEN", null) }
    val qrToken = remember { mutableStateOf<String?>(null) }

    // auth view model
    val authRepository = AuthRepository()
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authRepository, context)
    )

    // Permission view model
    val permissionRepository = remember { PermissionRepository() }
    val permissionViewModel: PermissionViewModel = viewModel(factory = PermissionViewModelFactory(repository = permissionRepository, context = context))

    // camera view model
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel: CameraPresViewModel = viewModel()
    val patroliViewModel: PatroliViewModel = viewModel()


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
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Profile.route) {
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
        composable(Screen.ChangePassword.route){
            ChangePasswordScreen(navCtrl = navController, token = token ?: "")
        }
        composable(Screen.Success.route){ backStackEntry ->
            SuccessScreen(
                navCtrl = navController,
                message = backStackEntry.arguments?.getString("message") ?: "",
                route = backStackEntry.arguments?.getString("route") ?: ""
            )

        }
        composable(Screen.Home.route){
            com.example.mobileapptechnobit.ui.HomeScreen(navCtrl = navController)
        }
        composable(Screen.History.route){
            HistoryScreen(navCtrl = navController, token = token ?: "")
        }
        composable(Screen.DetailHistory.route){
            DetailPresenceScreen(navCtrl = navController)
        }
        composable(Screen.Schedule.route) {
            DetailScheduleScreen(navCtrl = navController, token = token ?: "")
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController, token = token ?: "")
        }
        composable(Screen.DetailProfile.route) {
            DetailProfileScreen(navController = navController, token = token ?: "")
        }
        composable(Screen.EditProfile.route) {
            EditProfile(navController, token = token ?: "")
        }
        composable(Screen.EditSukses.route) {
            EditSuksesScreen(navController, token = token ?: "")
        }
        composable(Screen.InfoPerusahaan.route) {
            InformasiPerusahaan(navController = navController, token = token ?: "")
        }
        composable(Screen.CameraPresensi.route) {
            val clockInTime = viewModel.getClockInTime(context)

            if (clockInTime > 0) {
                navController.navigate(Screen.ClockOut.route) {
                    popUpTo(Screen.CameraPresensi.route) { inclusive = true }
                }
            } else {
                CameraPresensi(viewModel, navController, token = token ?: "")
            }
        }
        composable(Screen.CameraPresensiCheck.route) {
            CameraPresensiCheck(viewModel, navController, context)
        }
        composable(Screen.PresensiSukses.route) {
            PresensiSuksesScreen(navController)
        }
        composable(Screen.ClockOut.route) {
            val clockInTime = viewModel.getClockInTime(context)

            ClockOutScreen(navController = navController, clockInTime = clockInTime, token = token ?: "", viewModel = viewModel) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        viewModel.sendClockOutToApi(token ?: "")
                        viewModel.clearClockInTime(context)
                        viewModel.clearSessionData(context)

                        withContext(Dispatchers.Main) {
                            navController.navigate(Screen.ClockOutSukses.route) {
                                popUpTo(Screen.ClockOut.route) { inclusive = true }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("ClockOut", "Error during Clock-Out", e)
                    }
                }
            }
        }
        composable(Screen.ClockOutSukses.route) {
            ClockOutSuksesScreen(navController, viewModel = viewModel, context = context)
        }
        composable(Screen.Permission.route){
            PermissionScreen(navCtrl = navController, token = token ?: "", viewModel = permissionViewModel)
        }
        composable(Screen.PermissionForm.route){
            PermitFormScreen(navCtrl = navController, token = token ?: "")
        }
        composable(Screen.DetailPermission.route){ backStackEntry ->
            val gson = Gson()
            val json = backStackEntry.arguments?.getString("permissionItem")
            DetailPermitScreen(
                navCtrl = navController,
                id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0,
                isRequester = backStackEntry.arguments?.getString("isRequester").toBoolean(),
                token = token ?: "",
                viewModel = permissionViewModel
            )
        }
        composable(Screen.Patroli.route) {
            val context = LocalContext.current
            val activity = context as Activity
            PatroliScreen(navCtrl = navController, activity = activity)
        }
        composable(
            route = Screen.CameraPatroli.route,
            arguments = listOf(navArgument("qrToken") { type = NavType.StringType })
        ) { entry ->
            val qrToken = entry.arguments?.getString("qrToken")?.let { Uri.decode(it) } ?: ""
            CameraPatroli(navCtrl = navController, token = token ?: "", qrToken = qrToken, viewModel = patroliViewModel)
        }
        composable(
            route = Screen.FormPatroli.route,
            arguments = listOf(navArgument("qrToken") { type = NavType.StringType })
        ) { entry ->
            val qrTokenValue = entry.arguments?.getString("qrToken") ?: ""
            FormPatroli(navCtrl = navController, token = token ?: "", qrToken = qrTokenValue, viewModelPat = patroliViewModel)
        }
        composable(Screen.PatroliSukses.route) {
            PatroliSuksesScreen(navController, token = token ?: "")
        }
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Walkthrough : Screen("walkthrough_screen")
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object ForgotPassword : Screen("forgot_password_screen")
    object ResetPassword : Screen("reset_password_screen/{email}")
    object ChangePassword : Screen("change_password")
    object Profile : Screen("profile_screen")
    object DetailProfile : Screen("detailprofile_screen")
    object EditProfile : Screen("edit_profile_screen")
    object EditSukses : Screen("edit_sukses_screen")
    object InfoPerusahaan : Screen("info_perusahaan_screen")
    object Success : Screen("success_screen/{message}/{route}")
    object Schedule: Screen("schedule_screen")
    object History : Screen("history_screen")
    object DetailHistory : Screen("detail_history_screen")
    object CameraPresensi : Screen("camera_presensi_screen")
    object CameraPresensiCheck : Screen("camera_check_screen")
    object PresensiSukses : Screen("presensi_sukses_screen")
    object ClockOut : Screen("clock_out_screen")
    object ClockOutSukses : Screen("clock_out_sukses_screen")

    object Permission : Screen("permission_screen")
    object PermissionForm : Screen("permission_form_screen")
//    object DetailPermission: Screen("detail_permission_screen/{id}")
    object DetailPermission: Screen("detail_permission_screen/{id}/{isRequester}")
    object Patroli: Screen("patroli_screen")
    object CameraPatroli : Screen("camera_patroli_screen/{qrToken}")
    object FormPatroli : Screen("form_patroli_screen/{qrToken}")
    object PatroliSukses : Screen("patroli_sukses_screen")
}