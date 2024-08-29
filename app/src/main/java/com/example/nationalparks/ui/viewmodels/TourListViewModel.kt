package com.example.livisnationalparks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livisnationalparks.model.data.TourRemoteSource
import com.example.livisnationalparks.ui.compose.contracts.ToursContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TourListViewModel @Inject constructor(private val remoteSource: TourRemoteSource): ViewModel() {

    var state by mutableStateOf(
        ToursContract.State(
            tours = listOf(),
            isLoading = true
        )
    )
        private set

    var effects = Channel<ToursContract.Effect>(UNLIMITED)
        private set

    init {
        viewModelScope.launch { getTours() }
    }

    private suspend fun getTours() {
        val tours = remoteSource.getTours()
        viewModelScope.launch {
            state = state.copy(tours = tours, isLoading = false)
            effects.send(ToursContract.Effect.DataWasLoaded)
        }
    }
}