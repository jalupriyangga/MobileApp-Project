package com.example.mobileapptechnobit.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    fun startSplashScreen(navigateToHome: () -> Unit) {
        viewModelScope.launch {
            delay(2500) // Delay selama 2.5 detik sebelum pindah ke home
            navigateToHome()
        }
    }
}