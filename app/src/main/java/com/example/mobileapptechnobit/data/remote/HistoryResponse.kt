package com.example.mobileapptechnobit.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Data class untuk response wrapper by Gson Array
@Parcelize
data class HistoryPresensiResponseItem(
    @SerializedName("id")
    val id: String? = "",

    @SerializedName("foto")
    val foto: String? = null,

    @SerializedName("lokasi")
    val lokasi: String = "",

    @SerializedName("nama")
    val nama: String = "",

    @SerializedName("shift")
    val shift: String? = null,

    @SerializedName("status")
    val status: String = "",

    @SerializedName("catatan")
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

// Data class untuk response wrapper by Gson Object
@Parcelize
data class HistoryPatroliResponse(
    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: List<HistoryPatroliResponseItem> = emptyList()
) : Parcelable
