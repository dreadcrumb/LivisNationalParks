package com.example.livisnationalparks.model

import java.util.Date

data class TourItem (
    val id: Int,
    val title: String,
    val shortDescription: String,
    val thumb: String,
    val startDate: Date,
    val endDate: Date,
    val price: Double
)