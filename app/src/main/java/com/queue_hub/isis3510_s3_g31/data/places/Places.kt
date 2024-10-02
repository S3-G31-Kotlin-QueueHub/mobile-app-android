package com.queue_hub.isis3510_s3_g31.data.places

data class Places(
    val id : String,
    val idFranquicia: String,
    val nombre: String,
    val direccion: String,
    val telefono: String,
    val latitud: Float,
    val longitud: Float,
    val urlImg: String
)
