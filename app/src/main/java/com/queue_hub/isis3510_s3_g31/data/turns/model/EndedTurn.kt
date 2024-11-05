package com.queue_hub.isis3510_s3_g31.data.turns.model

import com.google.firebase.Timestamp

data class EndedTurn(
    val idUser: String,
    val idPlace: String,
    val createdAt: Timestamp,
    val endedAt: Timestamp,
    val way: String,
    val status: String,
    val turnNumber: Long
)