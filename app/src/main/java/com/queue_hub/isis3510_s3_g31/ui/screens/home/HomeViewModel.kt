package com.queue_hub.isis3510_s3_g31.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel ( private val placesRepository: PlacesRepository): ViewModel(){
    var state by mutableStateOf(HomeViewState())
        private set

    init {
        viewModelScope.launch {
            delay(1000)
            state = state.copy(
                places = placesRepository.getCommonPlacesByUser("69aba19d-d8a3-4033-812f-146fa0cd1c98"),
                isLoading = false
            )
        }
    }
}