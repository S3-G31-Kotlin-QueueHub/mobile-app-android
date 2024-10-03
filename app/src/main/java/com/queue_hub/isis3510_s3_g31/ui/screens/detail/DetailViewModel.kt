package com.queue_hub.isis3510_s3_g31.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.places.Places
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import kotlinx.coroutines.launch

class DetailViewModel (private val placesRepository: PlacesRepository): ViewModel() {

    var state by mutableStateOf(DetailViewState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(
                place = getPlace(),
                isLoading = false
            )
        }
    }


    suspend fun getPlace() : Places{
            return placesRepository.getPlace()
    }



}

