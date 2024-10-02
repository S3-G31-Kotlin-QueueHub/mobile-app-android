package com.queue_hub.isis3510_s3_g31.ui.screens.recommended

import com.queue_hub.isis3510_s3_g31.data.places.Places

data class RecommendedViewState(
    val places: List<Places> = listOf(),
    val isLoading : Boolean = false
)
