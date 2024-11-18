package com.queue_hub.isis3510_s3_g31.data.places


import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.queue_hub.isis3510_s3_g31.data.places.local.PlacesDao
import com.queue_hub.isis3510_s3_g31.data.places.mapper.toDomain
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlace
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlaceFirestore
import com.queue_hub.isis3510_s3_g31.data.places.remote.PlacesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class PlacesRepository (
    private val placesDao: PlacesDao,
    private  val api: PlacesApi,
    private val db: FirebaseFirestore,
    private var place: Place = Place(
        id = "1",
        name = "No name",
        address = "No address",
        phone = "000-000-000",
        localization ="",
        image = "https://24ai.tech/es/wp-content/uploads/sites/5/2023/10/01_product_1_sdelat-kvadratnym-3-1.jpg",
        averageWaitingTime = 0,
        averageWaitingTimeLastHour = 0,
        averageScoreReview = 0f,
        bestAverageFrame = "empty"
    )
){

    suspend fun getAllPlaces() : List<Place>{

        val places = mutableListOf<Place>()
        val allPlacesRef = db.collection("places")

        try {
            val result = allPlacesRef.get().await()

            for (document in result) {
                val id = document.id
                val name = document.getString("name") ?: ""
                val address = document.getString("address") ?: ""
                val phone = document.getString("phone") ?: ""
                val localization = (document.get("localization") as? GeoPoint)?.let {
                    "${it.latitude}, ${it.longitude}"
                } ?: "0, 0"
                val image = document.getString("image") ?: ""
                val bestAverageFrame = document.getString("bestAverageFrame") ?: ""
                val averageWaitingTime = (document.get("averageWaitingTime") as? Number)?.toInt() ?: 0
                val averageWaitingTimeLastHour = (document.get("averageWaitingTimeLastHour") as? Number)?.toInt() ?: 0
                val averageScoreReview = (document.get("averageScoreReview") as? Number)?.toFloat() ?: 0f

                places.add(
                    Place(
                        id,
                        name,
                        address,
                        phone,
                        localization,
                        image,
                        averageWaitingTime,
                        averageWaitingTimeLastHour,
                        averageScoreReview,
                        bestAverageFrame
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("DatosPlaces", "Error getting documents: ", e)
        }

        return places
    }

    suspend fun getCommonPlaces(idUser: String): Flow<List<CommonPlace>> = callbackFlow {
        val commonPlacesRef = db.collection("commonPlaces").document(idUser)

        val commonPlacesSubscription = commonPlacesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                launch {
                    try {
                        if (!snapshot.exists()) {
                            trySend(emptyList())
                            return@launch
                        }

                        val commonPlacesData = snapshot.data?.get("commonPlaces") as? List<Map<String, Any>>

                        if (commonPlacesData == null) {
                            trySend(emptyList())
                            return@launch
                        }

                        val commonPlaces = coroutineScope {
                            commonPlacesData.map { commonPlace ->
                                async {
                                    val placeId = commonPlace["idPlace"] as? String ?: return@async null
                                    val lastVisit = commonPlace["lastVisit"] as? Timestamp ?: Timestamp.now()
                                    val visitCount = commonPlace["visitCount"] as? Long ?: 0L

                                    try {
                                        val placeDoc = db.collection("places").document(placeId).get().await()

                                        if (placeDoc.exists()) {
                                            CommonPlaceFirestore(
                                                id = placeId,
                                                name = placeDoc.getString("name") ?: "",
                                                address = placeDoc.getString("address") ?: "",
                                                phone = placeDoc.getString("phone") ?: "",
                                                image = placeDoc.getString("image") ?: "",
                                                lastVisit = lastVisit,
                                                city = placeDoc.getString("city") ?: "",
                                                localization = placeDoc.getGeoPoint("localization")  ?: GeoPoint(0.0, 0.0),
                                                type = placeDoc.getString("type") ?: "",
                                                visitCount = visitCount,
                                            ).toDomain()
                                        } else null
                                    } catch (e: Exception) {
                                        println("Error fetching place $placeId: ${e.message}")
                                        null
                                    }
                                }
                            }.awaitAll().filterNotNull()
                        }

                        trySend(commonPlaces)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
            }
        }

        awaitClose {
            commonPlacesSubscription.remove()
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
        return place
    }

    fun setPlace (place: Place){
        this.place = place
    }

    suspend fun getPlaces(): List<Place> {

        val entities = placesDao.getPlaces()
        val entitiesDomain = entities.map{
            it.toDomain()
        }
        return entitiesDomain
    }

}