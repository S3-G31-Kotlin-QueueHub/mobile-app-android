package com.queue_hub.isis3510_s3_g31.data.places.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceEntity(
    @PrimaryKey()
    val id : String,
    val name: String,
    val address: String,
    val phone: String,
    val localization: String,
    val image: String,
    val averageWaitingTime: Int,
    val averageWaitingTimeLastHour: Int,
    val averageScoreReview: Float,
    val bestAverageFrame: String
)
