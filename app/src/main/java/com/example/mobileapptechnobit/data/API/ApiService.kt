package com.example.mobileapptechnobit.data.API

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService{

    @Headers("Accept: application/json", "Content-Type: application/json", "Authorization: Bearer YOUR_ACCESS_TOKEN")
    @POST("v1/login")
    suspend fun login(@Body request: HashMap<String, String>): Response<ResponseBody>

    @POST("v1/android/request-otp")
    suspend fun requestOtp(@Body email: HashMap<String, String>): Response<ResponseBody>

    @POST("v1/android/verify-otp")
    suspend fun verifyOtp(@Body request: HashMap<String, String>): Response<ResponseBody>

    @POST("v1/android/reset-password")
    suspend fun resetPassword(@Body request: HashMap<String, String>): Response<ResponseBody>

}