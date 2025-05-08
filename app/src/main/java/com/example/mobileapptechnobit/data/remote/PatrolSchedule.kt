package com.example.mobileapptechnobit.data.remote

data class PatrolScheduleResponse(
    val tanggal: String,
    val waktu: String,
    val jam_mulai: String,
    val jam_selesai: String,
    val lokasi: String,
    val rekan_tugas: List<String>
)
