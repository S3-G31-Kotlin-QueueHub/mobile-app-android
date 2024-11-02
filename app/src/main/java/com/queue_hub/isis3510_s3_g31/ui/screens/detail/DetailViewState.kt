package com.queue_hub.isis3510_s3_g31.ui.screens.detail

import com.queue_hub.isis3510_s3_g31.data.places.Places

data class DetailViewState(
    val place: Places = Places(
        id = "1",
        idFranquicia = "Franquicia_123",
        nombre = "Café del Sol",
        direccion = "Calle 123, Bogotá",
        telefono = "555-1234",
        latitud = 4.60971,
        longitud = -74.08175,
        urlImg = "https://24ai.tech/es/wp-content/uploads/sites/5/2023/10/01_product_1_sdelat-kvadratnym-3-1.jpg",
        averageWaitingTime = 15,
        averageWaitingTimeLastHour = 10,
        averageScoreReview = 4.5f,
        bestAverageFrame = "12:00-14:00"
    )
)
