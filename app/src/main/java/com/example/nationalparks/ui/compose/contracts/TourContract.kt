package com.example.livisnationalparks.ui.compose.contracts

import com.example.livisnationalparks.model.TourItem

class ToursContract {

    data class State(
        val tours: List<TourItem> = listOf(),
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded: Effect()
    }
}