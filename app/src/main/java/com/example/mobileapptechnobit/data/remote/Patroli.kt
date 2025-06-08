package com.example.mobileapptechnobit.data.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class PatroliRequest(
    val photo_base64: String,
    val filename: String,
    val shift_id: Int,
    val place_id: Int,
    val catatan: String,
    val kondisi: String,
    val latitude: String,
    val longitude: String
)

@Parcelize
data class PatroliQrInfo(
    val id: Int,
    val company_id: Int,
    val code: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val description: String
) : Parcelable