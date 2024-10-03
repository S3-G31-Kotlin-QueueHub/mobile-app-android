package com.queue_hub.isis3510_s3_g31.data.places

import android.util.Log
import com.queue_hub.isis3510_s3_g31.data.places.local.PlacesDao
import com.queue_hub.isis3510_s3_g31.data.places.mapper.toDomain
import com.queue_hub.isis3510_s3_g31.data.places.mapper.toEntity
import com.queue_hub.isis3510_s3_g31.data.places.remote.PlacesApi


class PlacesRepository (
    private val placesDao: PlacesDao,
    private  val api: PlacesApi
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
    suspend fun getPlace (): Places{
        //TO DO
        return getPlaces()[0]
    }

    suspend fun getPlaces(): List<Places> {

        val entities = placesDao.getPlaces()
        val entitiesDomain = entities.map{
            it.toDomain()
        }
        return entitiesDomain
    }

}