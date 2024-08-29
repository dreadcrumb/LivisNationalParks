package com.example.nationalparks.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.nationalparks.model.TourItem
import com.example.nationalparks.model.data.TourRemoteSource
import com.example.nationalparks.ui.compose.contracts.ToursContract
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class Sorting {
    STANDARD, TOP5
}

@HiltViewModel
class TourListViewModel @Inject constructor(private val remoteSource: TourRemoteSource?,
                                            @ApplicationContext private val context: Context?): ViewModel() {

    var state by mutableStateOf(
        ToursContract.State(
            tours = listOf(),
            isLoading = true,
            sorting = Sorting.STANDARD
        )
    )
        set // for Preview
    // TODO: add interface to overwrite in view for preview

    var effects = Channel<ToursContract.Effect>(UNLIMITED)
        private set

    init {
        viewModelScope.launch {
            getTours()
            if (context != null) {
                preloadImages(context, state.tours.map { it.thumb })
            }
        }
    }

    private suspend fun getTours() {
        val tours = remoteSource?.getTours() ?: state.tours
        viewModelScope.launch {
            state = state.copy(tours = tours, isLoading = false)
            effects.send(ToursContract.Effect.DataWasLoaded)
        }
    }

    fun getSortedTours(): List<TourItem> {
        return sortTours(state.tours)
    }

    fun sortTours(tours: List<TourItem>): List<TourItem> {
        if (tours.isEmpty()) return listOf()

        return if (state.sorting == Sorting.STANDARD) {
            tours
        } else {
            tours.subList(0, 4)
        }
    }

    fun setSorting(sorting: Sorting) {
        if (state.sorting != sorting) {
            state.sorting = sorting
        }
    }

    suspend fun preloadImages(context: Context, urls: List<String>) {
        val imageLoader = ImageLoader(context)

        urls.forEach { url ->
            val request = ImageRequest.Builder(context)
                .data(url)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()

            withContext(Dispatchers.IO) {
                imageLoader.execute(request)
            }
        }
    }
}