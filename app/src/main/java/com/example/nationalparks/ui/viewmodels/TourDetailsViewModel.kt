package com.example.nationalparks.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.nationalparks.model.ContactItem
import com.example.nationalparks.model.data.TourRemoteSource
import com.example.nationalparks.model.response.ContactResponse
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
            phoneNumber = "",
            loadingState = LoadingState.LOADING
        )
    )

    override val state: State<TourDetailsContract.State> get() = _state

    init {
        viewModelScope.launch {
            getTourDetails()
            if (context != null && state.value.tour != null) {
                loadImage(context, state.value.tour!!.image)
            }
            getPhoneNumber()
        }
    }

    override fun callCompany() {
        val phone = state.value.phoneNumber
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse(phone)
        }
        context?.startActivity(intent)
    }

    private suspend fun getTourDetails() {
        val tourId = stateHandle?.get<Int>(NavigationKeys.Arg.TOUR_ID)
        if (tourId == null) {
            _state.value = _state.value.copy(loadingState = LoadingState.ERROR)
            return
        }

        val tourDetails = remoteSource?.getTourDetails(tourId)
        if (tourDetails == null) {
            _state.value = _state.value.copy(loadingState = LoadingState.ERROR)
            return
        }
        _state.value = _state.value.copy(tour = tourDetails, loadingState = LoadingState.SUCCESS)
    }

    private suspend fun loadImage(context: Context, url: String) {
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
        _state.value = _state.value.copy(phoneNumber = remoteSource?.getContactDetails()?.phone ?: "")
    }
}