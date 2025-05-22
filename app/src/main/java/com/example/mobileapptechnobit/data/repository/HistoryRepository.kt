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

    suspend fun fetchHistoryPatroli(token: String): Result<List<HistoryPatroliResponseItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val authToken = "Bearer $token"
                val response = apiService.getHistoryPatroli(authToken)
                Log.d("HistoryRepository", "Response: $response")
                if (response.data.isNotEmpty()) {
                    Result.success(response.data)
                } else {
                    Result.failure(Exception("Data kosong: ${response.message}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            } as Result<List<HistoryPatroliResponseItem>>
        }
    }
}
