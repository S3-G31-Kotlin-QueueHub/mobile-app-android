package com.queue_hub.isis3510_s3_g31.ui.screens.recommended

import com.queue_hub.isis3510_s3_g31.data.places.model.Place

data class RecommendedViewState(
    val places: List<Place> = listOf(),
    val isLoading : Boolean = true,
    val selectedCategory: RecommendedCategory = RecommendedCategory.ALL,
    val isConnected : Boolean = false
)
