package com.example.mobileapptechnobit.data.remote

data class PresensiResponse(
    val message: String,
    val data: PresensiData
)

data class Presensi(
    val status: String,
    val photo_data: String,
    val filename: String,
    val company_place_id: Int,
    val note: String
)

data class PresensiData(
    val id: Int,
    val employee_id: Int,
    val company_place_id: Int,
    val checked_in_at: String,
    val checked_out_at: String?, // Nullable karena mungkin null saat Clock In
    val status: String,
    val note: String,
    val photo_path: String,
    val updated_at: String,
    val created_at: String
)

data class ClockOutRequest(
    val status: String,
    val filename: String,
    val company_place_id: Int,
    val note: String
)