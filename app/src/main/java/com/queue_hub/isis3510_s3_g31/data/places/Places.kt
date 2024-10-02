package com.queue_hub.isis3510_s3_g31.data.places

data class Places(
    val id : String,
    val idFranquicia: String,
    val nombre: String,
    val direccion: String,
    val telefono: String,
    val latitud: Double,
    val longitud: Double,
    val urlImg: String,
    val averageWaitingTime: Int,
    val averageWaitingTimeLastHour: Int,
    val averageScoreReview: Float,
    val bestAverageFrame: String
)

// 1. Establecimiento son menos tiempo de espera en la ultima hora  /places/shortest-time-lasthour
// 2. Cuales son los tiempos de espera mas cortos en un establecimiento en particular /places/
// 3. Common places by user /places/common-by-user/{id}
