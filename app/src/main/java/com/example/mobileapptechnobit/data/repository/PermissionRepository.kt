package com.example.mobileapptechnobit.data.repository

import android.util.Log
import com.example.mobileapptechnobit.data.API.ApiClient
import com.example.mobileapptechnobit.data.remote.AlternatePermissionResponse
import com.example.mobileapptechnobit.data.remote.PermissionResponse
import okhttp3.ResponseBody
import retrofit2.Response

class PermissionRepository {

    private val api = ApiClient.apiService

    suspend fun sendPermission(token: String, date: String, permission: String, reason: String): Response<ResponseBody> {
        val request = hashMapOf(
            "date" to date,
            "permission" to permission,
            "reason" to reason
        )
        val authToken = "Bearer $token"
        Log.d("PermissionRepository", "Auth Token: $authToken")
        Log.d("PermissionRepository", "date: $date")
        Log.d("PermissionRepository", "permission: $permission")
        Log.d("PermissionRepository", "Alasan: $reason")

        return api.sendPermission(authToken, request)
    }

    suspend fun deletePermission(token: String, id: Int): Response<ResponseBody> {
        val authToken = "Bearer $token"
        val request = hashMapOf(
            "id" to id
        )
        return api.deletePermission(authToken, request)
    }

    suspend fun fetchPermission(token: String): Response<PermissionResponse> {

        val authToken = "Bearer $token"
        return api.fetchPermission(authToken)
    }

    suspend fun fetchAlternatePermission(token: String): Response<AlternatePermissionResponse> {

        val authToken = "Bearer $token"
        return api.fetchAlternatePermission(authToken)
    }

    suspend fun sendApproval(token: String, id: Int, approval: String): Response<ResponseBody> {
        val request: HashMap<String, Any> = hashMapOf(
            "permit_id" to id,
            "status" to approval,
        )
        val authToken = "Bearer $token"

        return api.sendApproval(authToken, request)
    }

    suspend fun sendAlternateApproval(token: String, id: Int, approval: String): Response<ResponseBody> {
        val request: HashMap<String, Any> = hashMapOf(
            "id" to id,
            "status" to approval,
        )
        val authToken = "Bearer $token"

        return api.sendAlternateApproval(authToken, request)
    }
}