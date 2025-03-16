package com.example.mobileapptechnobit.data.repository

import android.content.Context
import android.util.Log
import com.example.mobileapptechnobit.data.API.ApiClient
import com.example.mobileapptechnobit.data.API.UserProfileResponse
import com.example.mobileapptechnobit.data.remote.Employees
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(private val context: Context) {

    suspend fun fetchEmployeesProfile(token: String): Employees? {
        return withContext(Dispatchers.IO) {
            try {
                val authToken = "Bearer $token"
                Log.d("ProfileRepository", "Auth Token: $authToken")

                val response = ApiClient.apiService.fetchEmployeeProfile(authToken)
                Log.d("ProfileRepository", "Response Code: ${response.code()}")
                Log.d("ProfileRepository", "Response Message: ${response.message()}")
                Log.d("ProfileRepository", "Response Headers: ${response.headers()}")
                Log.d("ProfileRepository", "Request Headers: ${response.raw().request().headers()}")

                if (response.isSuccessful) {
                    val profileData = response.body()?.data
                    Log.d("ProfileRepository", "Profile fetch successful: $profileData")
                    profileData
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

    suspend fun updateProfile(token: String, updateFields: Map<String, String>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val authToken = "Bearer $token"
                Log.d("ProfileRepository", "Auth Token: $authToken")

                Log.d("ProfileRepository", "Sending update profile request: $updateFields")

                val response = ApiClient.apiService.updateEmployeeProfile(authToken, updateFields)
                Log.d("ProfileRepository", "Response Code: ${response.code()}")
                Log.d("ProfileRepository", "Response Message: ${response.message()}")
                Log.d("ProfileRepository", "Response Headers: ${response.headers()}")
                Log.d("ProfileRepository", "Request Headers: ${response.raw().request().headers()}")

                val responseBody = response.body()?.toString()
                Log.d("ProfileRepository", "Response Body: $responseBody")

                if (response.isSuccessful) {
                    Log.d("ProfileRepository", "Profile update successful")
                    true
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ProfileRepository", "Error: ${response.code()}, Message: ${response.message()}, Body: $errorBody")
                    false
                }
            } catch (e: Exception) {
                Log.e("ProfileRepository", "Exception: ${e.message}")
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun getUserProfile(token: String): UserProfileResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val authToken = "Bearer $token"
                Log.d("ProfileRepository", "Auth Token: $authToken")

                val response = ApiClient.apiService.getUserProfile(authToken)
                Log.d("ProfileRepository", "Response Code: ${response.code()}")
                Log.d("ProfileRepository", "Response Message: ${response.message()}")
                Log.d("ProfileRepository", "Response Headers: ${response.headers()}")
                Log.d("ProfileRepository", "Request Headers: ${response.raw().request().headers()}")

                if (response.isSuccessful) {
                    val profileData = response.body()
                    Log.d("ProfileRepository", "User profile fetch successful: $profileData")
                    profileData
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