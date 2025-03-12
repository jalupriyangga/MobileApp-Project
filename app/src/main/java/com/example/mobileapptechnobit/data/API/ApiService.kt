package com.example.mobileapptechnobit.data.API

import com.example.mobileapptechnobit.data.remote.EmployeeResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService{

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("v1/login")
    suspend fun login(@Body request: HashMap<String, String>): Response<ResponseBody>

    @POST("v1/android/request-otp")
    suspend fun requestOtp(@Body email: HashMap<String, String>): Response<ResponseBody>

    @POST("v1/android/reset-password")
    suspend fun resetPassword(@Body request: HashMap<String, String>): Response<ResponseBody>

    @POST("v1/android/verify-otp")
    suspend fun verifyOtp(@Body request: HashMap<String, String>): Response<ResponseBody>

    @GET("v1/profile/employee")
    suspend fun fetchEmployeeProfile(@Header("Authorization") authorization: String): Response<EmployeeResponse>
}