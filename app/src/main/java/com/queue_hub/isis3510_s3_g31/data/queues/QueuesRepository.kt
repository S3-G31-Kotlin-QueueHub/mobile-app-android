package com.queue_hub.isis3510_s3_g31.data.queues


import com.google.firebase.firestore.FirebaseFirestore
import com.queue_hub.isis3510_s3_g31.data.queues.model.PreviousQueue
import com.queue_hub.isis3510_s3_g31.data.queues.model.Queue
import com.queue_hub.isis3510_s3_g31.data.queues.remote.FirebaseQueueData
import com.queue_hub.isis3510_s3_g31.data.queues.service.QueueDataService
import kotlinx.coroutines.flow.Flow


class QueuesRepository(
    private val queueDataService: QueueDataService = FirebaseQueueData(FirebaseFirestore.getInstance()),
) {
    suspend fun getQueue(idPlace: String): Flow<Queue> {
        return queueDataService.getQueue(idPlace)
    }

    suspend fun getPreviousUserQueues(idUser: String): Flow<List<PreviousQueue>> {
        return queueDataService.getPreviousUserQueues(idUser)
    }
}

