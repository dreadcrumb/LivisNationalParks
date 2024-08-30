package com.example.nationalparks.model.data

import com.example.nationalparks.model.ContactItem
import com.example.nationalparks.model.TourDetailsItem
import com.example.nationalparks.model.TourItem
import com.example.nationalparks.model.response.ContactResponse
import com.example.nationalparks.model.response.TourDetailsResponse
import com.example.nationalparks.model.response.ToursResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TourRemoteSource @Inject constructor(private val tourApi: TourApi) {
    suspend fun getTours(): List<TourItem> = withContext(Dispatchers.IO) {
        return@withContext tourApi.getTours().mapRemoteToursToItems()
    }

    suspend fun getTop5Tours(): List<TourItem> = withContext(Dispatchers.IO) {
        return@withContext tourApi.getTop5Tours().mapRemoteToursToItems()
    }

    suspend fun getTourDetails(id: Int): TourDetailsItem = withContext(Dispatchers.IO) {
        return@withContext tourApi.getTourDetails(id).mapRemoteTourDetailsToItem()
    }

    suspend fun getContactDetails(): ContactItem = withContext(Dispatchers.IO) {
        return@withContext tourApi.getContactDetails().mapToItem()
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

    private fun TourDetailsResponse.mapRemoteTourDetailsToItem(): TourDetailsItem {
        return TourDetailsItem(
            id = this.id,
            title = this.title,
            shortDescription = this.shortDescription,
            description = this.description,
            thumb = this.thumb,
            image = this.image,
            startDate = parseDate(this.startDate),
            endDate = parseDate(this.endDate),
            price = this.price
        )
    }

    private fun ContactResponse.mapToItem(): ContactItem {
        return ContactItem(
            companyName = this.companyName,
            street = this.street,
            country = this.country,
            phone = this.phone,
        )
    }

    private fun parseDate(dateString: String): String {
        if (dateString.isBlank()) {
            return "XX"
        }
        val offsetDateTime =
            OffsetDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return offsetDateTime.format(formatter)
    }
}