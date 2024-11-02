package com.queue_hub.isis3510_s3_g31.ui.screens.detail

import com.queue_hub.isis3510_s3_g31.data.places.model.Place

data class DetailViewState(
    val place: Place = Place(
        id = "1",
        name = "Café del Sol",
        address = "Calle 123, Bogotá",
        phone = "555-1234",
        localization = "",
        image = "https://24ai.tech/es/wp-content/uploads/sites/5/2023/10/01_product_1_sdelat-kvadratnym-3-1.jpg",
        averageWaitingTime = 15,
        averageWaitingTimeLastHour = 10,
        averageScoreReview = 4.5f,
        bestAverageFrame = "12:00-14:00"
    )
)
