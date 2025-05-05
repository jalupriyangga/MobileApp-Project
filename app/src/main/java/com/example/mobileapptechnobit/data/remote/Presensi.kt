package com.example.mobileapptechnobit.data.remote

data class PresensiResponse(
    val message: String,
    val data: Presensi
)

data class Presensi(
    val status: String,
    val photo_data: String,
    val filename: String,
    val company_place_id: Int,
    val note: String
)
