package com.queue_hub.isis3510_s3_g31.ui.screens.home

import com.queue_hub.isis3510_s3_g31.data.places.Places

data class HomeViewState (
    val places: List<Places> = listOf(),
    val isLoading : Boolean = true
)