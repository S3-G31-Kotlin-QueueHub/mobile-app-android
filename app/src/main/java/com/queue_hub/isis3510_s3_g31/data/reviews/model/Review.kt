package com.queue_hub.isis3510_s3_g31.data.reviews.model

import com.google.firebase.Timestamp

data class Review (
    val score: Long,
    val comment: String,
    val userName: String,
    val date: Timestamp
)