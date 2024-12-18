package com.queue_hub.isis3510_s3_g31.data

import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlace
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.queues.QueuesRepository
import com.queue_hub.isis3510_s3_g31.data.queues.model.PreviousQueue
import com.queue_hub.isis3510_s3_g31.data.queues.model.Queue
import com.queue_hub.isis3510_s3_g31.data.reviews.ReviewsRepository
import com.queue_hub.isis3510_s3_g31.data.reviews.model.Review
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.turns.model.Turn
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationData
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationProvider
import com.queue_hub.isis3510_s3_g31.utils.network_services.NetworkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class DataLayerFacade(
    private val placesRepository: PlacesRepository,
    private val turnsRepository: TurnsRepository,
    private val queuesRepository: QueuesRepository,
    private val usersRepository: UsersRepository,
    private val locationProvider: LocationProvider,
    private val networkManager: NetworkManager,
    private val reviewsRepository: ReviewsRepository
    ) {

    suspend fun setPlaceToDetail(idPlace: String) {
        this.placesRepository.setPlace(idPlace)
    }
    suspend fun getPlaceToDetail():Place? {
        return this.placesRepository.getPlace()
    }
    suspend fun getTurnsNumberByUser(userId:String):Int{
        return this.turnsRepository.getTurnsLength(userId)
    }

    suspend fun getAllPlaces() : List<Place> {
        return placesRepository.getAllPlaces()
    }

    suspend fun getLessWaitingTimeLastHour(): List<Place>{
        return placesRepository.getLessWaitingTimeLastHour()
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
    suspend fun addTurn(idUser: String, idPlace: String):Boolean{
        return turnsRepository.addTurn(idUser,idPlace )
    }
    suspend fun cancelTurn(idUser: String): Boolean {
        return turnsRepository.cancelTurn(idUser)
    }

    suspend fun getReviews(idPlace: String): Flow<List<Review>> {
        return reviewsRepository.getReviews(idPlace)
    }
    
    suspend fun requestLocationUpdates(): Flow<LocationData> {
        return locationProvider.requestLocationUpdates()
    }

    suspend fun checkNetworkConnection(): Flow<Boolean> {
        return networkManager.isConnected
    }

    suspend fun logIn(email: String, password: String) {
        return usersRepository.logIn(email, password)
    }

    suspend fun signUp(email: String, password: String, phone: String, name: String) {
        return usersRepository.signUp(email, password, phone, name)
    }


}