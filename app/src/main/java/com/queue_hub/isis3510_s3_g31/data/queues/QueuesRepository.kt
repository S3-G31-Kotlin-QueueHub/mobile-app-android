package com.queue_hub.isis3510_s3_g31.data.queues

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.queue_hub.isis3510_s3_g31.data.places.mapper.toDomain
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlaceFirestore
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.queues.model.PreviousQueue
import com.queue_hub.isis3510_s3_g31.data.queues.model.Queue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class QueuesRepository(
    private val db: FirebaseFirestore
) {
    suspend fun getQueue(idPlace:String): Flow<Queue> = callbackFlow{
        val queueRef = db.collection("queues").document(idPlace)

        val queueSubscription = queueRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if(snapshot != null){
                try {

                    CoroutineScope(Dispatchers.IO).launch{
                        val placeSnapshot = db.collection("places").document(idPlace).get().await()
                        val placeData = placeSnapshot.data
                        val placeImage = placeData?.get("image") as? String ?: ""
                        val placeName = placeData?.get("name") as? String ?: ""

                        val queueData = snapshot.data
                        val queue = Queue(
                            name = placeName,
                            currentTurnNumber = queueData?.get("currentTurnNumber") as? Long ?: 0,
                            image = placeImage
                        )
                        println("Queue: $queue")
                        trySend(queue)
                    }
                } catch (e: Exception) {
                    close(e)
                }
            }
        }
        awaitClose {
            queueSubscription.remove()
        }
    }


    suspend fun getPreviousUserQueues(idUser: String): Flow<List<PreviousQueue>> = callbackFlow {
         val commonPlacesRef = db.collection("commonPlaces").document(idUser)

         val commonPlacesSubscription = commonPlacesRef.addSnapshotListener { snapshot, error ->
             if (error != null) {
                 return@addSnapshotListener
             }

             if (snapshot != null ) {
                 try {

                     if (!snapshot.exists()) {
                         trySend(emptyList())
                         return@addSnapshotListener
                     }

                     val commonPlacesData = snapshot.data?.get("commonPlaces") as? List<Map<String, Any>>
                     println("commonPlacesData: $commonPlacesData")
                     val previousQueues = commonPlacesData?.map{
                         PreviousQueue(
                             name = it["name"] as? String ?: "",
                             image = it["image"] as? String ?: "",
                             lastVisit = it["lastVisit"] as? Timestamp ?: Timestamp.now(),
                             city = it["city"] as? String ?: "",
                             address = it["address"] as? String ?: "",
                         )
                     } ?: emptyList()
                     val sortedQueues = previousQueues.sortedByDescending { it.lastVisit }
                     trySend(sortedQueues)
                 } catch (e: Exception) {
                     close(e)
                 }
             }
         }
         awaitClose {
             commonPlacesSubscription.remove()
         }
    }

}