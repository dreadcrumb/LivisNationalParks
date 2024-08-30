package com.example.nationalparks.utils

import android.content.Context
import android.util.DisplayMetrics

fun getScreenWidth(context: Context): Int {
    val metrics: DisplayMetrics = context.resources.displayMetrics
    return metrics.widthPixels
}