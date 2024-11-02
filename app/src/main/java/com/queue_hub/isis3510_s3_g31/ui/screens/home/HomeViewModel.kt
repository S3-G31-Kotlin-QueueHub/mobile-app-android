package com.queue_hub.isis3510_s3_g31.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.model.CommonPlace
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel ( private val placesRepository: PlacesRepository): ViewModel(){


    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val uiState: StateFlow<HomeViewState> = _uiState

    init {
        getCommonPlaces()
    }

    private fun getCommonPlaces(){
        viewModelScope.launch {
            placesRepository.getCommonPlaces("NuHTofO46WPkFZksEL7tRFheP593").collect{ commonPlaces ->
                _uiState.value = HomeViewState.Success(commonPlaces)
            }
        }
    }
}