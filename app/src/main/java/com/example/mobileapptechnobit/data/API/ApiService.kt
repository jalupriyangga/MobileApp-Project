package com.example.mobileapptechnobit.data.API

import com.example.mobileapptechnobit.data.remote.EmployeeResponse
import com.example.mobileapptechnobit.data.remote.PatrolScheduleResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

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

    @PUT("v1/android/change-password")
    suspend fun changePassword(@Header("Authorization") authorization: String, @Body request: HashMap<String, String>): Response<ResponseBody>

    @GET("v1/profile/employee")
    suspend fun fetchEmployeeProfile(@Header("Authorization") authorization: String): Response<EmployeeResponse>

    @PUT("v1/profile/employee")
    suspend fun updateEmployeeProfile(@Header("Authorization") authorization: String, @Body request: Map<String, String>): Response<Unit>

    @GET("v1/profile/user")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<UserProfileResponse>

    @GET("v1/android/jadwal-patroli")
    suspend fun getPatrolSchedules(
        @Header("Authorization") token: String,
        @Query("date") date: String? = null,
        @Query("company_id") companyId: Int? = null
    ): Response<List<PatrolScheduleResponse>>

}