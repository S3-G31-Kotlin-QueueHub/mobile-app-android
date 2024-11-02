package com.queue_hub.isis3510_s3_g31.data.places.mapper

import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.places.local.entity.PlaceEntity
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlace
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlaceFirestore
import com.queue_hub.isis3510_s3_g31.data.places.remote.PlacesResponseItem


fun Place.toEntity(): PlaceEntity{
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




fun PlaceEntity.toDomain() : Place {
    return Place(
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

fun PlacesResponseItem.toDomain() : Place {
    val finalLatitud = this.latitud.toDouble()
    val finalLongitud = this.longitud.toDouble()
    val finalAWT = this.averageWaitingTime.toDouble().toInt()
    val finalAWTLastHour = this.averageWaitingTimeLastHour.toDouble().toInt()
    val finalAverageScoreReview = (0..5).random()
    return Place(
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


fun CommonPlaceFirestore.toDomain() : CommonPlace {
    return CommonPlace(
        id = this.id,
        name = this.name,
        address = this.address,
        phone = this.phone,
        image = this.image,
        lastVisit = this.lastVisit.toString(),
        city = this.city
    )
}