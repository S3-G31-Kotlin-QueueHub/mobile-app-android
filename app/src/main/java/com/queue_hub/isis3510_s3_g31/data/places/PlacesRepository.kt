package com.queue_hub.isis3510_s3_g31.data.places

import com.queue_hub.isis3510_s3_g31.data.places.local.PlacesDao
import com.queue_hub.isis3510_s3_g31.data.places.mapper.toDomain
import com.queue_hub.isis3510_s3_g31.data.places.mapper.toEntity


class PlacesRepository (
    private val placesDao: PlacesDao
){


    suspend fun getCommonPlacesByUser(idUser : String ): List<Places>{
        //TO DO
        return getPlaces()
    }

    suspend fun getRecommendedPlaces (): List<Places>{
        //TO DO
        return getPlaces()
    }

    suspend fun getPlaces(): List<Places> {
        val entities = placesDao.getPlaces()
        val entitiesDomain = entities.map{
            it.toDomain()
        }
        return entitiesDomain
    }

    suspend fun fillDatabase(){
        val place1 = Places(
            id = "1",
            idFranquicia = "1",
            nombre = "Crepes And Waffles Centro Mayor",
            direccion = "Centro Mayor",
            telefono = "3218764",
            latitud = 4.591245810553451,
            longitud = -74.12377198465478,
            urlImg = "https://centromayor.com.co/wp-content/uploads/2023/02/logo_crepes_centro_mayor_1-2.png",
            averageScoreReview = 4.3F,
            averageWaitingTime = 23,
            averageWaitingTimeLastHour = 10,
            bestAverageFrame = "Tarde"
        )

        val place2 = Places(
            id = "2",
            idFranquicia = "1",
            nombre = "Crepes And Waffles Plaza Central",
            direccion = "Plaza Central",
            telefono = "32187532",
            latitud = .63220432741325,
            longitud = -74.11570628558987,
            urlImg = "https://centromayor.com.co/wp-content/uploads/2023/02/logo_crepes_centro_mayor_1-2.png",
            averageScoreReview = 4.0F,
            averageWaitingTime = 1504,
            averageWaitingTimeLastHour = 155,
            bestAverageFrame = "Mañana"
        )

        placesDao.insertPlace(place1.toEntity())
        placesDao.insertPlace(place2.toEntity())
    }


}