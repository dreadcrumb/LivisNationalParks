package com.example.nationalparks.model

data class TourDetailsItem(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val description: String,
    val thumb: String,
    val image: String,
    val startDate: String,
    val endDate: String,
    val price: Double
)