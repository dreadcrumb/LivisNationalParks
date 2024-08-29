package com.example.nationalparks.ui.compose.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun hasNetworkComposable(): Boolean {
    // Get the current context using LocalContext
    val context = LocalContext.current

    // Obtain the ConnectivityManager from the context
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Check network connectivity status
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}