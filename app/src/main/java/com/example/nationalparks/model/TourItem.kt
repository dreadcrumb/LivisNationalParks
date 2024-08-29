package com.example.nationalparks.model

import java.util.Date

data class TourItem (
    val id: String,
    val title: String,
    val shortDescription: String,
    val thumb: String,
    val startDate: String,
    val endDate: String,
    val price: Double
)