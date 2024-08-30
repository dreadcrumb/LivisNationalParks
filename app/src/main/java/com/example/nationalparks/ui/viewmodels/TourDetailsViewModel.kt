package com.example.nationalparks.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.nationalparks.model.data.TourRemoteSource
import com.example.nationalparks.ui.compose.contracts.LoadingState
import com.example.nationalparks.ui.compose.contracts.TourDetailsContract
import com.example.nationalparks.ui.compose.navigation.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface TourDetailsViewModelInterface {
    val state: State<TourDetailsContract.State>
    fun initialize(useHeight: Boolean)
    fun initializeManually(tourId: Int)
    fun callCompany()
}

@HiltViewModel
class TourDetailsViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle?,
    private val remoteSource: TourRemoteSource?,
    @ApplicationContext private val context: Context?
) : ViewModel(), TourDetailsViewModelInterface {
    var _state = mutableStateOf(
        TourDetailsContract.State(
            tour = null,
            image = null,
            useHeight = false,
            phoneNumber = "",
            loadingState = LoadingState.LOADING
        )
    )

    override val state: State<TourDetailsContract.State> get() = _state

    // Init

    init {
        Log.i("TourDetailsViewModel", "init")
        loadDetails()
    }

    private fun loadDetails() {
        Log.i("TourDetailsViewModel", "loadDetails")
        viewModelScope.launch {
            getTourDetails()
            if (context != null && state.value.tour != null) {
                loadImage(context, state.value.tour!!.image)
            }
            getPhoneNumber()
        }
    }

    // Interface functions

    override fun initialize(useHeight: Boolean) {
        Log.i("TourDetailsViewModel", "Initialize, useHeight: $useHeight")
        val tourId = stateHandle?.get<Int>(NavigationKeys.Arg.TOUR_ID)
        Log.i("TourDetailsViewModel", "Initialize, tourId: $tourId")
        if (tourId != null && tourId > -1) {
            _state.value.useHeight = useHeight
            loadDetails()
        }
    }

    override fun initializeManually(tourId: Int) {
        Log.i("TourDetailsViewModel", "InitializeManually, tourId: $tourId")
        stateHandle?.set(NavigationKeys.Arg.TOUR_ID, tourId)
        _state.value.useHeight = true
        loadDetails()
    }

    override fun callCompany() {
        Log.i("TourDetailsViewModel", "CallCompany")
        val phone = state.value.phoneNumber
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse(phone)
        }
        context?.startActivity(intent)
    }

    // Private functions

    private suspend fun getTourDetails() {
        Log.i("TourDetailsViewModel", "getTourDetails")
        val tourId = stateHandle?.get<Int>(NavigationKeys.Arg.TOUR_ID)
        Log.i("TourDetailsViewModel", "getTourDetails, tourId: $tourId")
        if (tourId == null) {
            _state.value = _state.value.copy(loadingState = LoadingState.ERROR)
            return
        } else if (tourId < 0) (
                return
                )

        val tourDetails = remoteSource?.getTourDetails(
            tourId,
            useHeight = state.value.useHeight
        )
        if (tourDetails == null) {
            _state.value = _state.value.copy(loadingState = LoadingState.ERROR)
            return
        }
        _state.value = _state.value.copy(tour = tourDetails, loadingState = LoadingState.SUCCESS)
    }

    private suspend fun loadImage(context: Context, url: String) {
        Log.i("TourDetailsViewModel", "loadImage, url: $url")
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()

        withContext(Dispatchers.IO) {
            val result = (imageLoader.execute(request) as? SuccessResult)?.drawable
            if (result != null) {
                _state.value = _state.value.copy(image = result)
            }
        }
    }

    private suspend fun getPhoneNumber() {
        Log.i("TourDetailsViewModel", "getPhoneNumber")
        _state.value =
            _state.value.copy(phoneNumber = remoteSource?.getContactDetails()?.phone ?: "")
    }
}