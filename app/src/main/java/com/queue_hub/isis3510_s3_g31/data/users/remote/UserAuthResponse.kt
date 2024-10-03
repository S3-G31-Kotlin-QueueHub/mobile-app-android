package com.queue_hub.isis3510_s3_g31.data.users.remote

data class UserAuthResponse(
    val expireAt: String,
    val id: String,
    val token: String
)