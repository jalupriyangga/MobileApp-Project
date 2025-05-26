package com.example.mobileapptechnobit.data.repository


import com.example.mobileapptechnobit.data.API.ApiClient
import com.example.mobileapptechnobit.data.remote.CompanyProfileResponse
import retrofit2.Response

class CompanyProfileRepository {
    suspend fun fetchCompanyProfile(token: String): Response<CompanyProfileResponse> {
        return ApiClient.apiService.getCompanyProfile("Bearer $token")
    }
}