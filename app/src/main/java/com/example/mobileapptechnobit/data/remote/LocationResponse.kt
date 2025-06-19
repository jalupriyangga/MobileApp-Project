package com.example.mobileapptechnobit.data.remote

import com.google.gson.annotations.SerializedName

class LocationResponse (
    val data: CompanyLocation
)

data class CompanyLocation(
    @SerializedName("latitude") val latitude: Double? = 0.0,
    @SerializedName("longitude") val longitude: Double? = 0.0,
)
