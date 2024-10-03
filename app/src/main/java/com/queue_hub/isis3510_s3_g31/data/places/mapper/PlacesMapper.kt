package com.queue_hub.isis3510_s3_g31.data.places.mapper

import com.queue_hub.isis3510_s3_g31.data.places.Places
import com.queue_hub.isis3510_s3_g31.data.places.local.entity.PlaceEntity
import com.queue_hub.isis3510_s3_g31.data.places.remote.PlacesResponseItem
import kotlin.math.max


fun Places.toEntity(): PlaceEntity{
    return PlaceEntity(
        id = this.id,
        idFranquicia = this.idFranquicia,
        nombre = this.nombre,
        direccion = this.direccion,
        telefono = this.telefono,
        latitud = this.latitud,
        longitud = this.longitud,
        urlImg = this.urlImg,
        averageWaitingTime = this.averageWaitingTime,
        averageWaitingTimeLastHour = this.averageWaitingTimeLastHour,
        averageScoreReview = this.averageScoreReview,
        bestAverageFrame = this.bestAverageFrame
    )
}


fun PlaceEntity.toDomain() : Places{
    return Places(
        id = this.id,
        idFranquicia = this.idFranquicia,
        nombre = this.nombre,
        direccion = this.direccion,
        telefono = this.telefono,
        latitud = this.latitud,
        longitud = this.longitud,
        urlImg = this.urlImg,
        averageWaitingTime = this.averageWaitingTime,
        averageWaitingTimeLastHour = this.averageWaitingTimeLastHour,
        averageScoreReview = this.averageScoreReview,
        bestAverageFrame = this.bestAverageFrame
    )
}

fun PlacesResponseItem.toDomain() : Places{
    val finalLatitud = this.latitud.toDouble()
    val finalLongitud = this.longitud.toDouble()
    val finalAWT = this.averageWaitingTime.toDouble().toInt()
    val finalAWTLastHour = this.averageWaitingTimeLastHour.toDouble().toInt()
    val finalAverageScoreReview = (0..5).random()
    return Places(
        id = this.id,
        idFranquicia = this.idFranquicia,
        nombre = this.nombre,
        direccion = this.direccion,
        telefono = this.telefono,
        latitud = finalLatitud,
        longitud = finalLongitud,
        urlImg = this.urlImg,
        averageWaitingTime = finalAWT,
        averageWaitingTimeLastHour = finalAWTLastHour,
        averageScoreReview = finalAverageScoreReview.toFloat(),
        bestAverageFrame = this.betterTime
    )
}

