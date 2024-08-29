package com.example.livisnationalparks.model.response


import com.google.gson.annotations.SerializedName
import java.util.Date

data class TourResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("shortDescription") val shortDescription: String,
    @SerializedName("thumb") val thumb: String,
    @SerializedName("startDate") val startDate: Date,
    @SerializedName("endDate") val endDate: Date,
    @SerializedName("price") val price: Double
)