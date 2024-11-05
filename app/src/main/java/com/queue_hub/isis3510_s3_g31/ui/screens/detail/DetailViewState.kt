package com.queue_hub.isis3510_s3_g31.ui.screens.detail

import com.queue_hub.isis3510_s3_g31.data.places.model.Place

data class DetailViewState(
    val place: Place = Place(
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
    ), val onQueue: Int = 0
)
