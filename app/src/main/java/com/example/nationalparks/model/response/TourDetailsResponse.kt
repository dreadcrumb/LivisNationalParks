package com.example.nationalparks.model.response

import com.google.gson.annotations.SerializedName

data class TourDetailsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("shortDescription") val shortDescription: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumb") val thumb: String,
    @SerializedName("image") val image: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("price") val price: Double
)