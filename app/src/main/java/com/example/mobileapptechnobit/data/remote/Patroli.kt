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
    val placeId: Int,
    val latitude: String,
    val longitude: String
) : Parcelable