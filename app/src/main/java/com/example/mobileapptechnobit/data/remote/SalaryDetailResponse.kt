package com.example.mobileapptechnobit.data.remote

data class SalaryDetailResponse(
    val data: List<SalaryItem>
)

data class SalaryItem(
    val periode: String,
    val tanggal: String,
    val status: String,
    val detail: SalaryDetail
)

data class SalaryDetail(
    val total_gaji: Int,
    val gaji_pokok: Int,
    val tunjangan: Int,
    val bonus: Int,
    val pajak: Int,
    val insentif_pajak: Int
)