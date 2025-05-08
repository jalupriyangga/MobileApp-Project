package com.example.mobileapptechnobit.data.repository

import android.util.Log
import com.example.mobileapptechnobit.data.API.ApiService
import com.example.mobileapptechnobit.data.remote.HistoryResponse
import com.example.mobileapptechnobit.data.remote.HistoryResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryRepository(private val apiService: ApiService) {
    suspend fun fetchHistory(token: String): List<HistoryResponseItem>? {
        return withContext(Dispatchers.IO) {
            try {
                val authToken = "Bearer $token"
                val response = apiService.getHistoryPresensi(authToken)

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("HistoryRepository", "Fetched: $body")
                    return@withContext body
                } else {
                    Log.e("HistoryRepository", "Error response: ${response.errorBody()?.string()}")
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.e("HistoryRepository", "Exception: ${e.message}", e)
                return@withContext null
            }
        }
    }
}
