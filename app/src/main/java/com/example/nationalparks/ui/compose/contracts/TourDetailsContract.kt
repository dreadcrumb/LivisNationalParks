package com.example.nationalparks.ui.compose.contracts

import com.example.nationalparks.model.TourDetailsItem

class TourDetailsContract {
    data class State(
        val tour: TourDetailsItem?,
        var loadingState: LoadingState = LoadingState.LOADING
    )
}