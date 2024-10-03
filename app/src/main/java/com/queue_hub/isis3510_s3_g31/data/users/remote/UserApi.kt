package com.queue_hub.isis3510_s3_g31.data.users.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    companion object{
        val instance2 = Retrofit.Builder().baseUrl("http://192.168.1.6:8000/").addConverterFactory(
            MoshiConverterFactory.create()).client(
            OkHttpClient.Builder().build()
        ).build().create(UserApi::class.java)
    }

    @POST("users/auth")
    suspend fun login(@Body loginRequest: LoginRequest): UserAuthResponse
}