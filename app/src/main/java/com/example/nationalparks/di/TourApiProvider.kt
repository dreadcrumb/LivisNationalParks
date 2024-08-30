package com.example.nationalparks.di

import android.content.Context
import android.util.Log
import com.example.nationalparks.model.data.API.API_URL
import com.example.nationalparks.model.data.TourApi
import com.example.nationalparks.utils.hasNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class TourApiProvider {
    companion object {
        private const val DAY_IN_SECONDS = 60 * 60 * 24 // 60 seconds x 60 minutes x 24 hours
        private const val WEEK_IN_SECONDS = DAY_IN_SECONDS * 7
        private const val CACHE_SIZE_5MB = 5L * 1024 * 1024 // 5MB, Long
    }

    @Provides
    @Singleton
    fun provideAuthInterceptorOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        Log.i("ApiProvider", "Providing AuthInterceptorOkHttpClient")
        val cacheSize = CACHE_SIZE_5MB
        val cache = Cache(context.cacheDir, cacheSize)
        return OkHttpClient.Builder().cache(cache).addInterceptor { chain ->
            var request = chain.request()
            request = if (hasNetwork(context)) {
                Log.i("ApiProvider", "Connected to internet, max cache age = 1 day")
                request.newBuilder().header("Cache-Control", "public, max-age=$DAY_IN_SECONDS")
                    .build()
            } else {
                Log.i("ApiProvider", "Connected to internet, max cache age = 1 day")
                request.newBuilder().header(
                    "Cache-Control", "public, only-if-cached, max-stale=$WEEK_IN_SECONDS"
                ).build()
            }
            chain.proceed(request)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        Log.i("ApiProvider", "Providing Retrofit")
        return Retrofit.Builder().client(okHttpClient)
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTourApiService(
        retrofit: Retrofit
    ): TourApi.Service {
        Log.i("ApiProvider", "Providing ClubApiService")
        return retrofit.create(TourApi.Service::class.java)
    }
}