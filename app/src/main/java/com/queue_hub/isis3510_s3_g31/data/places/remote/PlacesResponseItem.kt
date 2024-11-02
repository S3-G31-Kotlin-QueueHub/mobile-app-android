package com.queue_hub.isis3510_s3_g31.data.places.remote

data class PlacesResponseItem(
    val averageWaitingTime: String,
    val averageWaitingTimeLastHour: String,
    val betterTime: String,
    val cont: String,
    val contLastHour: String,
    val address: String,
    val id: String,
    val localization: String,
    val name: String,
    val phone: String,
    val image: String
)