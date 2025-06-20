package com.example.mobileapptechnobit.data.remote

import java.io.File

data class PresensiResponse(
    val success: Boolean,
    val message: String,
    val data: PresensiData,
    val errors: Map<String, List<String>>?
)

data class Presensi(
    val employee_id: Int,
    val company_place_id: Int,
    val photo_file: File,
    val user_note: String,
    val is_manual: Boolean
)

data class PresensiData(
    val employee_id: Int,
    val company_place_id: Int,
    val status: String,
    val clock_in: String,
    val clock_out: String?,
    val photo_path: String,
    val user_note: String
)

data class ClockOutRequest(
    val status: String,
    val filename: String,
    val company_place_id: Int,
    val note: String
)