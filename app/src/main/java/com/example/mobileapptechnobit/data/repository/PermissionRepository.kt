package com.example.mobileapptechnobit.data.repository

import android.util.Log
import com.example.mobileapptechnobit.data.API.ApiClient
import com.example.mobileapptechnobit.data.remote.Permission
import com.example.mobileapptechnobit.data.remote.PermissionResponseItem
import okhttp3.ResponseBody
import retrofit2.Response

class PermissionRepository {

    private val api = ApiClient.apiService

    suspend fun sendPermission(token: String, date: String, permission: String, reason: String): Response<ResponseBody> {
        val request = hashMapOf(
            "date" to date,
            "permission" to permission,
            "alasan" to reason
        )
        val authToken = "Bearer $token"
        Log.d("PermissionRepository", "Auth Token: $authToken")
        Log.d("PermissionRepository", "date: $date")
        Log.d("PermissionRepository", "permission: $permission")
        Log.d("PermissionRepository", "Alasan: $reason")

        return api.sendPermission(token, request)
    }

    suspend fun deletePermission(id: String): Response<Unit> {
        return api.deletePermission(id)
    }

    suspend fun fetchPermission(token: String): Response<List<PermissionResponseItem>> {
        return api.fetchPermission(token)
    }
}