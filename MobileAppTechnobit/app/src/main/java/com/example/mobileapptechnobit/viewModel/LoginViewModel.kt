package com.example.mobileapptechnobit.viewModel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapptechnobit.repository.LoginRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class LoginState {
    IDLE, SUCCESS, ERROR, FORBIDDEN
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val loginRepository = LoginRepository(application.applicationContext)

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
    var loginState by mutableStateOf(LoginState.IDLE)
    var errorMessage by mutableStateOf("")

    fun performLogin(email: String, password: String) {
        viewModelScope.launch {
            loginRepository.login(email, password, { response ->
                handleLoginSuccess(response)
            }, { error ->
                handleLoginError(error)
            })
        }
    }

    private fun handleLoginSuccess(response: JSONObject) {
        val status = response.optInt("status", -1)
        val message = response.optString("message", "Unknown Error")

        if (status == 200 && message == "Login berhasil") {
            val sharedPreferences = getApplication<Application>().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
            loginState = LoginState.SUCCESS
        } else if (status == 403) {
            errorMessage = "Akun anda salah"
            loginState = LoginState.FORBIDDEN
        } else {
            errorMessage = message
            loginState = LoginState.ERROR
        }
    }

    private fun handleLoginError(error: String) {
        errorMessage = error
        loginState = LoginState.ERROR
    }

    fun isLoggedIn(): Boolean {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    fun logout() {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("is_logged_in", false).apply()
        loginState = LoginState.IDLE
    }
}