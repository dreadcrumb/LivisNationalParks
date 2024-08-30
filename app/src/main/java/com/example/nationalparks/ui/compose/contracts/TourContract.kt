package com.example.nationalparks.ui.compose.contracts

import com.example.nationalparks.model.TourItem
import com.example.nationalparks.ui.viewmodels.Sorting

class ToursContract {

    data class State(
        val tours: List<TourItem> = listOf(),
        var loadingState: LoadingState = LoadingState.LOADING,
        var sorting: Sorting = Sorting.STANDARD
    )
}