package com.queue_hub.isis3510_s3_g31.data.places.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PlacesApi {

    companion object{
        val instance = Retrofit.Builder().baseUrl("http://192.168.1.6:8000/").addConverterFactory(MoshiConverterFactory.create()).client(
            OkHttpClient.Builder().build()
        ).build().create(PlacesApi::class.java)
    }

    @GET("places/shortest-time-lasthour")
    suspend fun getShortestTimePlacesLastHour() : List<PlacesResponseItem>

    @GET("places/common-by-user/{id}")
    suspend fun getCommonPlacesByUserId(@Path("id") userId: String ): List<PlacesResponseItem>
}