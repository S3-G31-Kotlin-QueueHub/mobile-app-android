package com.queue_hub.isis3510_s3_g31.data

import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlace
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.queues.QueuesRepository
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class DataLayerFacade(
    private val placesRepository: PlacesRepository,
    private val turnsRepository: TurnsRepository,
    private val queuesRepository: QueuesRepository,
    private val usersRepository: UsersRepository
) {

    suspend fun setPlaceToDetail(place: Place) {
        this.placesRepository.setPlace(place)
    }

    suspend fun getAllPlaces() : List<Place> {
        return placesRepository.getAllPlaces()
    }

    suspend fun getIdUser(): String {
        return usersRepository.userId.first()
    }

    suspend fun getCommonPlaces(idUser: String): Flow<List<CommonPlace>> {
        return placesRepository.getCommonPlaces(idUser)
    }

}