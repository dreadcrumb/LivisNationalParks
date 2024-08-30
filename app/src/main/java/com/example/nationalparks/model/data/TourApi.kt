package com.example.nationalparks.model.data

import android.content.Context
import com.example.nationalparks.model.response.TourDetailsResponse
import com.example.nationalparks.model.response.ToursResponse
import com.example.nationalparks.utils.getScreenWidth
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TourApi @Inject constructor(
    private val service: Service,
    @ApplicationContext private val context: Context
) {
    suspend fun getTours(): List<ToursResponse> = service.getTours()
    suspend fun getTop5Tours(): List<ToursResponse> = service.getTop5Tours()
    suspend fun getTourDetails(id: Int): TourDetailsResponse = service.getTourDetails(
        id = id,
        w = getScreenWidth(context),
        h = getScreenWidth(context) / 2
    )

    interface Service {
        @Headers("Accept: application/json")
        @GET("projekte/imaginary/api/tours/")
        suspend fun getTours(): List<ToursResponse>

        @Headers("Accept: application/json")
        @GET("projekte/imaginary/api/tours/top5/")
        suspend fun getTop5Tours(): List<ToursResponse>

        @Headers("Accept: application/json")
        @GET("projekte/imaginary/api/tours/")
        suspend fun getTourDetails(
            @Query("id") id: Int,
            @Query("w") w: Int,
            @Query("h") h: Int
        ): TourDetailsResponse
    }
}