package com.queue_hub.isis3510_s3_g31.ui.screens.recommended

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.places.Places
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import kotlinx.coroutines.launch

class RecommendedViewModel ( private val placesRepository: PlacesRepository): ViewModel() {

    var state by mutableStateOf(RecommendedViewState())
        private set

    init {
        viewModelScope.launch {
            placesRepository.fillDatabase()
            state = state.copy(
                places = getRecommendedPlaces(),
                isLoading = false
            )
        }
    }


    suspend fun getRecommendedPlaces() : List<Places>{
            return placesRepository.getRecommendedPlaces()
    }



}

