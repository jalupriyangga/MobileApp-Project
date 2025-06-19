package com.example.mobileapptechnobit.data.repository

import android.util.Log
import com.example.mobileapptechnobit.data.API.ApiClient
import com.example.mobileapptechnobit.data.remote.CompanyLocation
import com.example.mobileapptechnobit.data.remote.LocationResponse
import com.example.mobileapptechnobit.data.remote.PermissionResponse
import retrofit2.Response

class LocationRepository {
    private val api = ApiClient.apiService

    suspend fun getCompanyLocation(token: String): Response<CompanyLocation> {

        Log.d("location repository", "Masuk repository dengan token: $token")

        val authToken = "Bearer $token"

        return api.getCompanyLocation(authToken)
    }
}