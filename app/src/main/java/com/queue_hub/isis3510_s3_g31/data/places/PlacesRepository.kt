package com.queue_hub.isis3510_s3_g31.data.places


import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.queue_hub.isis3510_s3_g31.data.places.local.PlacesDao
import com.queue_hub.isis3510_s3_g31.data.places.mapper.toDomain
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlace
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlaceFirestore
import com.queue_hub.isis3510_s3_g31.data.places.remote.PlacesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class PlacesRepository (
    private val placesDao: PlacesDao,
    private  val api: PlacesApi,
    private val db: FirebaseFirestore
){

    suspend fun getCommonPlaces(idUser : String ): Flow<List<CommonPlace>> = callbackFlow {
        val commonPlacesRef = db.collection("commonPlaces").document(idUser)

        val commonPlacesSubscription = commonPlacesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                try {
                    val commonPlacesData = snapshot.data?.get("commonPlaces") as? List<Map<String, Any>>
                    println("commonPlacesData: $commonPlacesData")
                    val commonPlaces = commonPlacesData?.map {
                        CommonPlaceFirestore(
                            id = it["idPlace"] as String,
                            name = it["name"] as String,
                            address = it["address"] as String,
                            phone = it["phone"] as String,
                            image = it["image"] as String,
                            lastVisit = it["lastVisit"] as Timestamp,
                            city = it["city"] as String,
                            localization = it["localization"] as com.google.firebase.firestore.GeoPoint,
                            type = it["type"] as String,
                            visitCount = it["visitCount"] as Long
                        ).toDomain()
                    } ?: emptyList()
                    trySend(commonPlaces)
                } catch (e: Exception) {
                    close(e)
                }
            }
        }
        awaitClose {
            commonPlacesSubscription.remove()
        }
    }



    suspend fun getCommonPlacesByUser(idUser : String ): List<Place>{
        try {
            val response = api.getCommonPlacesByUserId(idUser)
            val places : List<Place> = response.map {
                it.toDomain()
            }
            return places

        }catch (e: Exception){
            return getPlaces()
        }
    }

    suspend fun getRecommendedPlaces (): List<Place>{
        try {
            val response = api.getShortestTimePlacesLastHour()
            val places : List<Place> = response.map {
                it.toDomain()
            }
            return places

        }catch (e: Exception){
            return getPlaces()
        }
    }
    suspend fun getPlace (): Place {
        //TO DO
        return getPlaces()[0]
    }

    suspend fun getPlaces(): List<Place> {

        val entities = placesDao.getPlaces()
        val entitiesDomain = entities.map{
            it.toDomain()
        }
        return entitiesDomain
    }

}