package com.queue_hub.isis3510_s3_g31.ui.screens.home

import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlace
import com.queue_hub.isis3510_s3_g31.data.places.model.Place

sealed class HomeViewState {
    object Loading : HomeViewState()
    data class Success(val commonPlaces: List<CommonPlace>) : HomeViewState()
    data class Error(val message: String) : HomeViewState()
}