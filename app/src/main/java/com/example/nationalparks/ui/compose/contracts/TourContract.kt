package com.example.nationalparks.ui.compose.contracts

import com.example.nationalparks.model.TourItem
import com.example.nationalparks.ui.viewmodels.Sorting

class ToursContract {

    data class State(
        val tours: List<TourItem> = listOf(),
        var isLoading: Boolean = false,
        var sorting: Sorting = Sorting.STANDARD
    )

    sealed class Effect {
        object DataWasLoaded : Effect()
        object SortingChanged : Effect()
    }
}