package com.example.nationalparks.model

data class TourItem(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val thumb: String,
    val startDate: String,
    val endDate: String,
    val price: Double
)