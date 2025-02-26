package com.example.sisteminformasimenejemensatpam

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sisteminformasimenejemensatpam.ui.HalamanLupaPassword
import com.example.sisteminformasimenejemensatpam.ui.HalamanPerbaruiPassword
import com.example.sisteminformasimenejemensatpam.ui.HalamanUbahPassword
import com.example.sisteminformasimenejemensatpam.ui.theme.SistemInformasiMenejemenSatpamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SistemInformasiMenejemenSatpamTheme {
                val navController = rememberNavController()
                val startDestination = "ubah password"

                NavHost(navController = navController, startDestination = startDestination) {
                    composable("ubah password"){ HalamanUbahPassword(navCtrl = navController) }
                    composable("perbarui password"){ HalamanPerbaruiPassword(navCtrl = navController) }
                    composable("lupa password"){ HalamanLupaPassword(navCtrl = navController)}
                }
//                LinkHandling(intent = intent)
            }
        }
    }
}

//@Composable
//fun LinkHandling(intent: Intent?) {
//    val navCtrl = rememberNavController()
//    val startDestination = "ubah password"
//    val deepLink = intent?.data?.path
//
//    NavHost(navController = navCtrl, startDestination = startDestination) {
//        composable("ubah password"){ HalamanUbahPassword(navCtrl = navCtrl) }
//        composable("perbarui password"){ HalamanPerbaruiPassword(navCtrl = navCtrl) }
//        composable("lupa password"){ HalamanLupaPassword(navCtrl = navCtrl)}
//    }
//    LaunchedEffect (deepLink) {
//        if (deepLink == "/reset-password"){
//            navCtrl.navigate("perbarui password")
//        }
//    }
//}