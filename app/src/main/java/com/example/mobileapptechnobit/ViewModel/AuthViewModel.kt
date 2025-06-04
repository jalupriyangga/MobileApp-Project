package com.example.mobileapptechnobit.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobileapptechnobit.data.repository.AuthRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

class AuthViewModel(private val repository: AuthRepository, private val context: Context) : ViewModel(){

    private val _loginMessage = MutableLiveData<String>()
    val loginMessage: LiveData<String> get() = _loginMessage

    private val _verifyOtpMessage = MutableLiveData<String>()
    val verifyOtpMessage: LiveData<String> get() = _verifyOtpMessage

    private val _requestOtpMessage = MutableLiveData<String>()
    val requestOtpMessage: LiveData<String> get() = _requestOtpMessage

    private val _resetPasswordMessage = MutableLiveData<String>()
    val resetPasswordMessage: LiveData<String> get() = _resetPasswordMessage

    private val _isOtpVerified = MutableLiveData<Boolean>()
    val isOtpVerified: LiveData<Boolean> get() = _isOtpVerified

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _changePasswordMessage = MutableLiveData<String>()
    val changePasswordMessage: LiveData<String> get() = _changePasswordMessage

    fun login(email: String, password:String){
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                if(response.isSuccessful) {
                    val responseBody = response.body()?.string() ?: "Empty response"
                    Log.d("login", responseBody)

                    // Simpan token ke SharedPreferences
                    val token = extractToken(responseBody)
                    if (token != null) {
                        saveToken(token)
                        _loginMessage.postValue("Login Berhasil")
                    } else {
                        _loginMessage.postValue("Login berhasil, tetapi token tidak ditemukan")
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Empty response"
                    Log.e("login", errorBody)
                    _loginMessage.postValue("Login gagal")
                }
            } catch (e: Exception) {
                Log.e("login", "Error: ${e.message}")
                _loginMessage.postValue("Error: ${e.message}")
            }
        }
    }

    private fun extractToken(responseBody: String): String? {
        return try {
            val json = JSONObject(responseBody)
            json.getJSONObject("data").optString("token", null)
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error parsing token: ${e.message}")
            null
        }
    }

    private fun saveToken(token: String) {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("AUTH_TOKEN", token)
            apply()
        }
    }

    fun requestOtp(email: String) {
        viewModelScope.launch {
            try {
                val response = repository.requestOtp(email)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string() ?: "Empty response"
                    Log.d("ForgotPassword", "Response: $responseBody")
                    _requestOtpMessage.value = "Link telah dikirim. Silakan cek email anda!"
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("ForgotPassword", "Failed: ${response.code()}, Error: $errorBody")
                    _requestOtpMessage.value = "Link gagal terkirim"
                }
            } catch (e: Exception) {
                Log.e("ForgotPassword", "Error: ${e.message}")
                _requestOtpMessage.postValue("Error: ${e.message}")

            }
        }
    }

    fun verifyOtp(email: String, otp: String){
        viewModelScope.launch {
            try {
                val response = repository.verifyOtp(email, otp)
                if(response.isSuccessful){
                    val responseBody = response.body()?.string() ?: "Empty response"
                    Log.d("VerifyOtp", "Response: $responseBody")
                    _verifyOtpMessage.value = "Verifikasi berhasil!"
                    _isOtpVerified.value = true
                } else{
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("ForgotPassword", "Failed: ${response.code()}, Error: $errorBody")
                    _verifyOtpMessage.value = "Verifikasi gagal!"
                }
            } catch (e: Exception){
                Log.e("ForgotPassword", "Error: ${e.message}")
                _verifyOtpMessage.postValue("Error: ${e.message}")
            }
        }
    }

    fun resetPassword(email: String, newPassword: String) {
        _isSuccess.value = false
        viewModelScope.launch {
            try {
                val response = repository.resetPassword(email, newPassword)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string() ?: "Empty response"
                    Log.d("ResetPassword", "Response: $responseBody")
                    _resetPasswordMessage.value = ("Password berhasil diperbarui!")
                    _isSuccess.value = true
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("ResetPassword", "Failed: ${response.code()}, Error: $errorBody")
                    _resetPasswordMessage.value = ("Gagal: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("ResetPassword", "Exception: ${e.message}")
                _resetPasswordMessage.postValue("Error: ${e.message}")
            }
        }
    }

    fun changePassword(token: String, currentPassword: String, newPassword: String){
        _isSuccess.value = false

        val authToken = "Bearer $token"
        Log.d("ProfileViewModel", "Auth Token: $authToken")

        viewModelScope.launch {
            try {
                val response = repository.changePassword(authToken, currentPassword, newPassword)
                if(response.isSuccessful){
                    val responseBody = response.body()?.string() ?: "Empty response"
                    Log.d("change password", "Response: $responseBody")
                    _changePasswordMessage.postValue("Password berhasil diperbarui!")
                    _isSuccess.value = true
                } else{
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("change password", "Failed: ${response.code()}, Error: $errorBody")
                    _changePasswordMessage.postValue("Gagal: $errorBody")
                }
            } catch (e: Exception){
                Log.e("change Password", "Exception: ${e.message}")
                _changePasswordMessage.postValue("Error: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                // Hapus token dari SharedPreferences
                val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    remove("AUTH_TOKEN")
                    apply()
                }
                Log.d("logout", "Logout successful")
            } catch (e: Exception) {
                Log.e("logout", "Error: ${e.message}")
            }
        }
    }
}