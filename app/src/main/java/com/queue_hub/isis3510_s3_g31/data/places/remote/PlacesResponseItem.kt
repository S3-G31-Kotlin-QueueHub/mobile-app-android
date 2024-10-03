package com.queue_hub.isis3510_s3_g31.data.places.remote

data class PlacesResponseItem(
    val averageWaitingTime: String,
    val averageWaitingTimeLastHour: String,
    val betterTime: String,
    val cont: String,
    val contLastHour: String,
    val direccion: String,
    val id: String,
    val idFranquicia: String,
    val latitud: String,
    val longitud: String,
    val nombre: String,
    val telefono: String,
    val urlImg: String
)