package com.example.nationalparks.ui.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
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
class TourListViewModel @Inject constructor(
    private val remoteSource: TourRemoteSource?,
    @ApplicationContext private val context: Context?
) : ViewModel() {

    var _state = mutableStateOf(
        ToursContract.State(
            tours = listOf(),
            isLoading = true,
            sorting = Sorting.STANDARD
        )
    )

    val state: State<ToursContract.State> get() = _state
    // TODO: add interface to overwrite in view for preview

    val allTours: List<TourItem> = listOf()

    var effects = Channel<ToursContract.Effect>(UNLIMITED)
        private set

    init {
        viewModelScope.launch {
            getTours()
            if (context != null) {
                preloadImages(context, state.value.tours.map { it.thumb })
            }
        }
    }

    private suspend fun getTours() {
        val tours = if (state.value.sorting == Sorting.STANDARD)
            remoteSource?.getTours()
                ?: listOf() // TODO: ask client if result should be sorted (by name or price f.e.)
        else
            remoteSource?.getTop5Tours()
                ?: listOf() // TODO: ask client if result should be sorted (by name or price f.e.)
        _state.value = _state.value.copy(tours = tours, isLoading = false)
        effects.send(ToursContract.Effect.DataWasLoaded)
    }

    fun setSorting(sorting: Sorting) {
        if (sorting != state.value.sorting) {
            _state.value = _state.value.copy(sorting = sorting)
            viewModelScope.launch {
                getTours()
            }
        }
    }

    private val imageCache = mutableMapOf<String, Drawable>()

    private suspend fun preloadImages(context: Context, urls: List<String>) {
        val imageLoader = ImageLoader(context)

        urls.forEach { url ->
            val request = ImageRequest.Builder(context)
                .data(url)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()

            withContext(Dispatchers.IO) {
                val result = (imageLoader.execute(request) as? SuccessResult)?.drawable
                if (result != null) {
                    imageCache[url] = result
                }
            }
        }
    }

    fun getCachedImage(url: String): Drawable? {
        return imageCache[url]
    }
}