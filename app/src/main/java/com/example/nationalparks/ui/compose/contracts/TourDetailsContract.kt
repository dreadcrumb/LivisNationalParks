package com.example.nationalparks.ui.compose.contracts

import android.graphics.drawable.Drawable
import com.example.nationalparks.model.TourDetailsItem

class TourDetailsContract {
    data class State(
        val tour: TourDetailsItem? = null,
        val image: Drawable? = null,
        val phoneNumber: String = "",
        var loadingState: LoadingState = LoadingState.LOADING
    )
}