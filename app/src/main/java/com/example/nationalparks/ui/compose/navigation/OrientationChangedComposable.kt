package com.example.nationalparks.ui.compose.navigation

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun OrientationChangedComposable(
    changedToLandscape: () -> Unit,
    changedToPortrait: () -> Unit
) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            changedToLandscape()
        }
        Configuration.ORIENTATION_PORTRAIT -> {
            changedToPortrait()
        }
        else -> {
            changedToPortrait() // Default to portrait
        }
    }
}