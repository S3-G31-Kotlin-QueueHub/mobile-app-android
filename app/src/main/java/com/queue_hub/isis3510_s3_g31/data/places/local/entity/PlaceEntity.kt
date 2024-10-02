package com.queue_hub.isis3510_s3_g31.data.places.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true )
    val id : String,
    val idFranquicia: String,
    val nombre: String,
    val direccion: String,
    val telefono: String,
    val latitud: Float,
    val longitud: Float,
    val urlImg: String
)
