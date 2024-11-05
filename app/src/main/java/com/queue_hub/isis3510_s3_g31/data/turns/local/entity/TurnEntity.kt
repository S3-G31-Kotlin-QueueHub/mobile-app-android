package com.queue_hub.isis3510_s3_g31.data.turns.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TurnEntity(
    @PrimaryKey()
    val idUser: String,
    val idPlace: String,
    val createdAt: String,
    val way: String,
    val status: String,
    val turnNumber: Long,
    val endedAt: String,
)