package com.queue_hub.isis3510_s3_g31.data.users.remote.model

import com.google.firebase.Timestamp

data class Turn(
    val id: String,
    val userId: String,
    val restaurantId: String,
    val createdAt: Timestamp
)