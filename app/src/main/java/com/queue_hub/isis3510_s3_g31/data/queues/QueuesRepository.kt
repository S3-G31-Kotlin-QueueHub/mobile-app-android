package com.queue_hub.isis3510_s3_g31.data.queues

import com.google.firebase.firestore.FirebaseFirestore
import com.queue_hub.isis3510_s3_g31.data.queues.model.Queue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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
                    val queueData = snapshot.data
                    val queue = Queue(
                        currentTurnNumber = queueData?.get("currentTurnNumber") as? Long ?: 0,
                    )
                    println("Queue: $queue")
                    trySend(queue)
                } catch (e: Exception) {
                    close(e)
                }
            }
        }
        awaitClose {
            queueSubscription.remove()
        }
    }
}