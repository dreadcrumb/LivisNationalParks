package com.example.nationalparks.model.data

import com.example.nationalparks.model.response.ToursResponse
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TourApi @Inject constructor(private val service: Service) {
    suspend fun getTours(): ToursResponse = service.getTours()

    interface Service  {
        @GET("projekte/imaginary/api/tours/")
        suspend fun getTours(): ToursResponse
    }
}