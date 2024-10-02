package com.queue_hub.isis3510_s3_g31.data.places.mapper

import com.queue_hub.isis3510_s3_g31.data.places.Places
import com.queue_hub.isis3510_s3_g31.data.places.local.entity.PlaceEntity


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