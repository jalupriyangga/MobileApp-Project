package com.example.mobileapptechnobit.data.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class HistoryResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: List<HistoryResponseItem>
)

@Parcelize
data class HistoryResponseItem(
    val id: String? = "",
    val foto: String? = null,
    val lokasi: String = "",
    val nama: String = "",
    val shift: String? = null,
    val status: String = "",
    val tanggal: String = ""
) : Parcelable