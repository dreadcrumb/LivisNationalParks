package com.example.nationalparks.ui.compose.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingBar() {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.size(500.dp)
    ) {
        CircularProgressIndicator(Modifier.fillMaxSize())
    }
}