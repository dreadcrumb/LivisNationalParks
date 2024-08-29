package com.example.livisnationalparks.model.data

import com.example.livisnationalparks.model.TourItem
import com.example.livisnationalparks.model.response.TourResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TourRemoteSource @Inject constructor(private val tourApi: TourApi) {
    private var cachedTours: List<TourItem>? = null

    suspend fun getTours(): List<TourItem> = withContext(Dispatchers.IO) {
        var cachedTours = cachedTours
        if (cachedTours == null) {
            cachedTours = tourApi.getTours().mapRemoteToursToItems()
            this@TourRemoteSource.cachedTours = cachedTours
        }
        return@withContext cachedTours
    }

    private fun List<TourResponse>.mapRemoteToursToItems(): List<TourItem> {
        return this.map { tour ->
            TourItem(
                id = tour.id,
                title = tour.title,
                shortDescription = tour.shortDescription,
                thumb = tour.thumb,
                startDate = tour.startDate,
                endDate = tour.endDate,
                price = tour.price
            )
        }
    }
}