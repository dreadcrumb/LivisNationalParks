package com.example.livisnationalparks.model.data

import com.example.livisnationalparks.model.response.TourResponse
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TourApi @Inject constructor(private val service: Service) {
    suspend fun getTours(): List<TourResponse> = service.getTours()

    interface Service  {
        @GET("tours.xml")
        suspend fun getTours(): List<TourResponse>
    }
}