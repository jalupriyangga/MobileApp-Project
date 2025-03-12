package com.example.mobileapptechnobit.data.remote

data class EmployeeResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Employees
)