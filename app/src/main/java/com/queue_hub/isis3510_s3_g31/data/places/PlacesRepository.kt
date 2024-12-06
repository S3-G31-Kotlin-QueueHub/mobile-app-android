package com.queue_hub.isis3510_s3_g31.data.places


import android.util.ArrayMap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.queue_hub.isis3510_s3_g31.data.clusters.Cluster
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class PlacesRepository (
    private val placesDao: PlacesDao,
    private  val api: PlacesApi,
    private val db: FirebaseFirestore,

    private var allPlacesCache: ArrayMap<String,Place>? = null,
    private var place:String = ""
){

    suspend fun getAllPlaces() : List<Place>{

        val places = mutableListOf<Place>()
        val allPlacesRef = db.collection("places")

        try {
            val result = allPlacesRef.get().await()
            this.allPlacesCache = ArrayMap(result.size() + 5) // Se crea el cache
            var document: DocumentSnapshot? = null
            var n  = result.size()
            for (i in 0 until n ){
                document = result.documents[i]
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

                val place = Place(
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
                this.allPlacesCache!![id] = place
                places.add(place)
            }
        } catch (e: Exception) {
            Log.e("DatosPlaces", "Error getting all places: ", e)
        }

        return places
    }

 fun getClusters(): Flow<List<Cluster>> = flow {
    val clusters = mutableListOf<Cluster>()
    val clustersRef = db.collection("clusters")

    try {
        var result = clustersRef.get().await()
        var document  :  DocumentSnapshot ? = null
        var num = result.size()
        for (i in 0  until num) {
            document  = result.documents[i]
            val id = document.id

            val num =(document.get("places_num") as? Int) ?: 0
            val centerMap = document.get("center") as? Map<String, Any>
            val location = if (centerMap != null) {
                val latitude = (centerMap["latitude"] as? Number)?.toDouble() ?: 0.0
                val longitude = (centerMap["longitude"] as? Number)?.toDouble() ?: 0.0
                LatLng(latitude, longitude)
            } else {
                LatLng(0.0, 0.0)
            }

            val cluster = Cluster(
                cluster_id = id,
                center = location,
                places_num = num,

            )
            clusters.add(cluster)
        }

        emit(clusters)
    } catch (e: Exception) {
        Log.e("getClusters", "Error fetching clusters: ", e)
        emit(emptyList())
    }
}
    suspend fun getLessWaitingTimeLastHour() : List<Place>{
        val places = mutableListOf<Place>()
        val lessWaitingTimeRef = db.collection("recommendedPlaces").document("lestTimeLastHour")
        try {
            val result = lessWaitingTimeRef.get().await()
            val dataMap = result.data ?: emptyMap()
            val keys = dataMap.keys.toList()

            for(place in keys){
                val place = this.allPlacesCache!![place]
                if (place != null) {
                    places.add(place)
                }
            }
        }catch (e: Exception){
            Log.e("DatosPlaces", "Error getLessWaitingTimeLastHour ", e)
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





    suspend fun getPlace() :Place?{
        println("Buscando lugar con ID: $place")
        val placeState = mutableStateOf<Place?>(null)
        try {
            val document = db.collection("places").document(place).get().await()
            if (document.exists()) {
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

                // Actualiza el estado observable con el nuevo objeto Place
                placeState.value = Place(
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
            } else {
                placeState.value = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            placeState.value = null
        }
        return placeState.value
    }


    fun setPlace (idPlace: String){
        this.place = idPlace
    }

    suspend fun getPlaces(): List<Place> {

        val entities = placesDao.getPlaces()
        val entitiesDomain = entities.map{
            it.toDomain()
        }
        return entitiesDomain
    }

}