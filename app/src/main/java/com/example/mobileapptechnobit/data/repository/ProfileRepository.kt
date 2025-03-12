package com.example.mobileapptechnobit.data.repository

import android.content.Context
import android.util.Log
import com.example.mobileapptechnobit.data.API.ApiClient
import com.example.mobileapptechnobit.data.remote.Employees
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProfileRepository(private val context: Context) {

    suspend fun fetchEmployeesProfile(token: String): Employees? {
        return withContext(Dispatchers.IO) {
            try {
                val authToken = "Bearer $token"
                Log.d("ProfileRepository", "Auth Token: $authToken")

                // Log URL endpoint
                val url = "https://app.arunikaprawira.com/api/v1/profile/employee"
                Log.d("ProfileRepository", "URL: $url")

                val response = ApiClient.apiService.fetchEmployeeProfile(authToken)
                Log.d("ProfileRepository", "Response Code: ${response.code()}")
                Log.d("ProfileRepository", "Response Message: ${response.message()}")
                Log.d("ProfileRepository", "Response Headers: ${response.headers()}")
                Log.d("ProfileRepository", "Request Headers: ${response.raw().request.headers}")

                if (response.isSuccessful) {
                    response.body()?.data
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ProfileRepository", "Error: ${response.code()}, Message: ${response.message()}, Body: $errorBody")
                    null
                }
            } catch (e: Exception) {
                Log.e("ProfileRepository", "Exception: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }
}