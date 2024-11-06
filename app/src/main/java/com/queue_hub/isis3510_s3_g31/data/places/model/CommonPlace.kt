package com.queue_hub.isis3510_s3_g31.data.places.model

data class CommonPlace(
    val id : String,
    val name: String,
    val address: String,
    val phone: String,
    val image: String,
    val lastVisit: String,
    val city: String,
    val bestArrivalTime: String
)