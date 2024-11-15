package com.queue_hub.isis3510_s3_g31.data.queues.model

import com.google.firebase.Timestamp

data class PreviousQueue (
    val name: String,
    val image: String,
    val lastVisit: Timestamp,
    val city: String,
    val address: String
)