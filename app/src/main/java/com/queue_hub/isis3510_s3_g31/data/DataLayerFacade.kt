package com.queue_hub.isis3510_s3_g31.data

import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.queues.QueuesRepository
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository

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

}