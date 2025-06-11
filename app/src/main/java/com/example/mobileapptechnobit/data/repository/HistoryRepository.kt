package com.example.mobileapptechnobit.data.repository

import android.util.Log
import com.example.mobileapptechnobit.data.API.ApiService
import com.example.mobileapptechnobit.data.remote.HistoryPatroliResponseItem
import com.example.mobileapptechnobit.data.remote.HistoryPresensiResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryRepository(private val apiService: ApiService) {
    suspend fun fetchHistoryPresensi(token: String): List<HistoryPresensiResponseItem>? {
        return withContext(Dispatchers.IO) {
            try {
                val authToken = "Bearer $token"
                Log.d("HistoryRepository", "Fetching presensi with token: Bearer ${token.take(10)}...")

                val response = apiService.getHistoryPresensi(authToken)

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("HistoryRepository", "Presensi response successful: ${body?.size} items")
                    return@withContext body
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("HistoryRepository", "Presensi error response: Code=${response.code()}, Body=$errorBody")
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.e("HistoryRepository", "Presensi exception: ${e.message}", e)
                return@withContext null
            }
        }
    }

    suspend fun fetchHistoryPatroli(token: String): Result<List<HistoryPatroliResponseItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val authToken = "Bearer $token"
                Log.d("HistoryRepository", "Fetching patroli with token: Bearer ${token.take(10)}...")

                val response = apiService.getHistoryPatroli(authToken)
                Log.d("HistoryRepository", "Patroli API response: ${response}")
                Log.d("HistoryRepository", "Patroli message: ${response.message}")
                Log.d("HistoryRepository", "Patroli data size: ${response.data.size}")

                // Debug: Print first few items
                response.data.take(3).forEach { item ->
                    Log.d("HistoryRepository", "Patroli item: ID=${item.id}, Date=${item.createdAt}, Location=${item.patrolLocation}")
                }

                if (response.data.isNotEmpty()) {
                    Log.d("HistoryRepository", "Patroli fetch successful: ${response.data.size} items")
                    Result.success(response.data)
                } else {
                    Log.w("HistoryRepository", "Patroli data empty: ${response.message}")
                    // Return empty list instead of failure for empty data
                    Result.success(emptyList())
                }
            } catch (e: Exception) {
                Log.e("HistoryRepository", "Patroli exception: ${e.message}", e)
                Result.failure(e)
            }
        }
    }
}