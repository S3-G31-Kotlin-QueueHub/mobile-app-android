package com.queue_hub.isis3510_s3_g31.data.queues.service

import com.queue_hub.isis3510_s3_g31.data.queues.model.PreviousQueue
import com.queue_hub.isis3510_s3_g31.data.queues.model.Queue
import kotlinx.coroutines.flow.Flow

interface QueueDataService {
    suspend fun getQueue(idPlace: String): Flow<Queue>
    suspend fun getPreviousUserQueues(idUser: String): Flow<List<PreviousQueue>>
}