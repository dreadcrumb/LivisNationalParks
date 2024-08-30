package com.example.nationalparks.model.response

import com.google.gson.annotations.SerializedName

data class ToursResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("shortDescription") val shortDescription: String,
    @SerializedName("thumb") val thumb: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("price") val price: Double
)