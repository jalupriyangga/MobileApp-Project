package com.example.mobileapptechnobit

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.mobileapptechnobit.ui.feature_history.HistoryScreen

class DetailHistoryActivity : ComponentActivity() {

    // Launcher for handling permission requests
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all { it.value }
            if (granted) {
                Toast.makeText(this, "Permissions Granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check and request required permissions
        checkAndRequestPermissions()

        // Set the Jetpack Compose UI
        setContent {
            MaterialTheme {
                Surface(color = Color.White) {

                    // Display the UI component
                    HistoryScreen(navCtrl = rememberNavController(), token = "dummy_token")
                }
            }
        }
    }

    // Function to check and request
    // necessary permissions
    private fun checkAndRequestPermissions() {
        val requiredPermissions = mutableListOf<String>()

        // Request WRITE_EXTERNAL_STORAGE only
        // for Android 9 and below
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        // Request permissions if any are needed
        if (requiredPermissions.isNotEmpty()) {
            requestPermissionsLauncher.launch(requiredPermissions.toTypedArray())
        }
    }
}