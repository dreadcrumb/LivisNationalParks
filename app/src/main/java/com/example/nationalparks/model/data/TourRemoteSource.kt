package com.example.nationalparks.model.data

import com.example.nationalparks.model.TourItem
import com.example.nationalparks.model.response.ToursResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TourRemoteSource @Inject constructor(private val tourApi: TourApi) {
    private var cachedTours: List<TourItem>? = null
    private var cachedTop5Tours: List<TourItem>? = null

    suspend fun getTours(): List<TourItem> = withContext(Dispatchers.IO) {
        var cachedTours = cachedTours
        if (cachedTours == null) {
            cachedTours = tourApi.getTours().mapRemoteToursToItems()
            this@TourRemoteSource.cachedTours = cachedTours
        }
        return@withContext cachedTours
    }

    suspend fun getTop5Tours(): List<TourItem> = withContext(Dispatchers.IO) {
        var cachedTop5Tours = cachedTop5Tours
        if (cachedTop5Tours == null) {
            cachedTop5Tours = tourApi.getTop5Tours().mapRemoteToursToItems()
            this@TourRemoteSource.cachedTop5Tours = cachedTop5Tours
        }
        return@withContext cachedTop5Tours
    }


    private fun List<ToursResponse>.mapRemoteToursToItems(): List<TourItem> {
        return this.map { tour ->
            TourItem(
                id = tour.id,
                title = tour.title,
                shortDescription = tour.shortDescription,
                thumb = tour.thumb,
                startDate = parseDate(tour.startDate),
                endDate = parseDate(tour.endDate),
                price = tour.price
            )
        }
    }

    private fun parseDate(dateString: String): String {
        if (dateString.isBlank()) {
            return "XX"
        }
        val offsetDateTime = OffsetDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return offsetDateTime.format(formatter)
    }
}