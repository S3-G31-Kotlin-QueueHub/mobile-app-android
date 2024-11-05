package com.queue_hub.isis3510_s3_g31.ui.screens.recommended

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecommendedViewModel ( private val placesRepository: PlacesRepository): ViewModel() {

    var state by mutableStateOf(RecommendedViewState())
        private set

    init {
        viewModelScope.launch {
            delay(100)
            state = state.copy(
                places = placesRepository.getRecommendedPlaces(),
                isLoading = false
            )
        }
    }




}

