package com.queue_hub.isis3510_s3_g31.data.places.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceEntity(
    @PrimaryKey()
    val id : String,
    val idFranquicia: String,
    val nombre: String,
    val direccion: String,
    val telefono: String,
    val latitud: Double,
    val longitud: Double,
    val urlImg: String,
    val averageWaitingTime: Int,
    val averageWaitingTimeLastHour: Int,
    val averageScoreReview: Float,
    val bestAverageFrame: String
)
