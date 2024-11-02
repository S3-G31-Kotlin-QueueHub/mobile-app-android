package com.queue_hub.isis3510_s3_g31.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.users.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(
    private val placesRepository: PlacesRepository,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel(){


    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val uiState: StateFlow<HomeViewState> = _uiState

    init {
        getCommonPlaces()
    }

    private fun getCommonPlaces(){

        viewModelScope.launch {
            val idUser = userPreferencesRepository.userId.first()
            println("idUser: $idUser")

            placesRepository.getCommonPlaces(idUser).collect{ commonPlaces ->
                _uiState.value = HomeViewState.Success(commonPlaces)
            }
        }
    }
}