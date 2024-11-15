package com.queue_hub.isis3510_s3_g31.data.queues.model

data class Queue (
    val name: String,
    val currentTurnNumber: Long,
    val image: String
)