package com.queue_hub.isis3510_s3_g31.data.places.model

import com.google.firebase.firestore.GeoPoint

data class Place(
    val id : String,
    val name: String,
    val address: String,
    val phone: String,
    var localization: String,
    val image: String,
    val averageWaitingTime: Int,
    val averageWaitingTimeLastHour: Int,
    val averageScoreReview: Float,
    val bestAverageFrame: String,
){
    // Constructor sin argumentos para Firebase
    constructor() : this(
        id = "",
        name = "",
        address = "",
        phone = "",
        localization = "",
        image = "",
        averageWaitingTime = 0,
        averageWaitingTimeLastHour = 0,
        averageScoreReview = 0f,
        bestAverageFrame = ""
    )
}

// 1. Establecimiento son menos tiempo de espera en la ultima hora  /places/shortest-time-lasthour
// 2. Cuales son los tiempos de espera mas cortos en un establecimiento en particular /places/
// 3. Common places by user /places/common-by-user/{id}
