package com.queue_hub.isis3510_s3_g31.data.turns.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface TurnApi {

    companion object{
        val instance2 = Retrofit.Builder().baseUrl("http://192.168.0.253:8000/").addConverterFactory(
            MoshiConverterFactory.create()).client(
            OkHttpClient.Builder().build()
        ).build().create(TurnApi::class.java)
    }

}