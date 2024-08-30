package com.example.nationalparks.model.data

import com.example.nationalparks.model.response.ToursResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TourApi @Inject constructor(private val service: Service) {
    suspend fun getTours(): List<ToursResponse> = service.getTours()
    suspend fun getTop5Tours(): List<ToursResponse> = service.getTop5Tours()

    interface Service  {
        @Headers("Accept: application/json")
        @GET("projekte/imaginary/api/tours/")
        suspend fun getTours(): List<ToursResponse>

        @Headers("Accept: application/json")
        @GET("projekte/imaginary/api/tours/top5/")
        suspend fun getTop5Tours(): List<ToursResponse>
    }
}