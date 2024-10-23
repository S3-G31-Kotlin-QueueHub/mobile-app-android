package com.queue_hub.isis3510_s3_g31.data.places


import com.queue_hub.isis3510_s3_g31.data.places.local.PlacesDao
import com.queue_hub.isis3510_s3_g31.data.places.mapper.toDomain
import com.queue_hub.isis3510_s3_g31.data.places.remote.PlacesApi


class PlacesRepository (
    private val placesDao: PlacesDao,
    private  val api: PlacesApi,
    private var place: Places = Places(
        id = "1",
        idFranquicia = "Franquicia_123",
        nombre = "Café del Sol",
        direccion = "Calle 123, Bogotá",
        telefono = "555-1234",
        latitud = 4.60971,
        longitud = -74.08175,
        urlImg = "https://24ai.tech/es/wp-content/uploads/sites/5/2023/10/01_product_1_sdelat-kvadratnym-3-1.jpg",
        averageWaitingTime = 15,
        averageWaitingTimeLastHour = 10,
        averageScoreReview = 4.5f,
        bestAverageFrame = "12:00-14:00"
    )
){

    suspend fun getCommonPlacesByUser(idUser : String ): List<Places>{
        try {
            val response = api.getCommonPlacesByUserId(idUser)
            val places : List<Places> = response.map {
                it.toDomain()
            }
            return places

        }catch (e: Exception){
            return getPlaces()
        }
    }

    suspend fun getRecommendedPlaces (): List<Places>{
        try {
            val response = api.getShortestTimePlacesLastHour()
            val places : List<Places> = response.map {
                it.toDomain()
            }
            return places

        }catch (e: Exception){
            return getPlaces()
        }
    }
    fun getPlace (): Places{
        //TO DO
        return place
    }
    fun setPlace (place: Places){
        this.place = place
    }

    suspend fun getPlaces(): List<Places> {

        val entities = placesDao.getPlaces()
        val entitiesDomain = entities.map{
            it.toDomain()
        }
        return entitiesDomain
    }

}