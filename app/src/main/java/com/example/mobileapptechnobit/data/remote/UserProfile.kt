package com.example.mobileapptechnobit.data.API

data class UserProfileResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: UserProfileData
)

data class UserProfileData(
    val id: Int,
    val role_id: Int,
    val name: String,
    val username: String,
    val email: String,
    val email_verified_at: String?,
    val photo: String,
    val created_at: String,
    val updated_at: String
)