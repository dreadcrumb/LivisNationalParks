package com.example.nationalparks.model.data

import android.content.Context
import com.example.nationalparks.model.response.ContactResponse
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
    // Tours
    suspend fun getTours(): List<ToursResponse> = service.getTours()
    suspend fun getTop5Tours(): List<ToursResponse> = service.getTop5Tours()
    suspend fun getTourDetails(id: Int, useHeight: Boolean): TourDetailsResponse = service.getTourDetails(
        id = id,
        w = if (useHeight) (getScreenWidth(context) / 2) else  getScreenWidth(context),
        h = if (useHeight) (getScreenWidth(context) / 4) else  (getScreenWidth(context) / 2),
    )

    // Other
    // TODO: Make own api class for non-tours?
    suspend fun getContactDetails(): ContactResponse = service.getContactDetails()

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

        @Headers("Accept: application/json")
        @GET("projekte/imaginary/api/contact/")
        suspend fun getContactDetails(): ContactResponse
    }
}