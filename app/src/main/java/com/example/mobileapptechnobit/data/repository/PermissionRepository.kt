package com.example.mobileapptechnobit.data.repository

import android.util.Log
import com.example.mobileapptechnobit.data.API.ApiClient
import com.example.mobileapptechnobit.data.remote.Permission
import retrofit2.Response

class PermissionRepository {

    private val api = ApiClient.apiService

    suspend fun fetchPermission(token: String): Response<Permission> {

        val authToken = "Bearer $token"
        Log.d("ProfileRepository", "Auth Token: $authToken")

        return api.getPermission(authToken)
    }
}