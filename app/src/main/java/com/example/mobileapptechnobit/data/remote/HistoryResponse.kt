package com.example.mobileapptechnobit.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryPresensiResponseItem(
    val id: String? = "",
    val foto: String? = null,
    val lokasi: String = "",
    val nama: String = "",
    val shift: String? = null,
    val status: String = "",
    val tanggal: String = ""
) : Parcelable

@Parcelize
data class HistoryPatroliResponseItem(
    @SerializedName("id")
    val id: Int? = 0,

    @SerializedName("shift_id")
    val shiftId: Int? = null,

    @SerializedName("place_id")
    val placeId: Int? = null,

    @SerializedName("patrol_location")
    val patrolLocation: String = "",

    @SerializedName("status")
    val status: String = "",

    @SerializedName("catatan")
    val catatan: String = "",

    @SerializedName("photo")
    val photo: String = "",

    @SerializedName("created_at")
    val createdAt: String = "",

    @SerializedName("updated_at")
    val updatedAt: String = "",

    @SerializedName("photo_url")
    val photoUrl: String = ""
) : Parcelable

// Data class untuk response wrapper lengkap
@Parcelize
data class HistoryPatroliResponse(
    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: List<HistoryPatroliResponseItem> = emptyList()
) : Parcelable
