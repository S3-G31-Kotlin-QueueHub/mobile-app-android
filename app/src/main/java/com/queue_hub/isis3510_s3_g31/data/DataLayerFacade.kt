package com.queue_hub.isis3510_s3_g31.data

import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlace
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.queues.QueuesRepository
import com.queue_hub.isis3510_s3_g31.data.queues.model.PreviousQueue
import com.queue_hub.isis3510_s3_g31.data.queues.model.Queue
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.turns.model.Turn
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationData
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class DataLayerFacade(
    private val placesRepository: PlacesRepository,
    private val turnsRepository: TurnsRepository,
    private val queuesRepository: QueuesRepository,
    private val usersRepository: UsersRepository,
    private val locationProvider: LocationProvider
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

    suspend fun getQueue(idPlace: String): Flow<Queue> {
        return queuesRepository.getQueue(idPlace)
    }
    suspend fun getPreviousUserQueues(idUser: String): Flow<List<PreviousQueue>> {
        return queuesRepository.getPreviousUserQueues(idUser)
    }

    suspend fun getTurn(idUser: String): Flow<Turn> {
        return turnsRepository.getTurn(idUser)
    }

    suspend fun cancelTurn(idUser: String): Boolean {
        return turnsRepository.cancelTurn(idUser)
    }

    suspend fun requestLocationUpdates(): Flow<LocationData> {
        return locationProvider.requestLocationUpdates()
    }








}