package com.queue_hub.isis3510_s3_g31.ui.screens.recommended

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.ui.screens.home.HomeViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RecommendedViewModel ( private val dataLayerFacade: DataLayerFacade): ViewModel() {


    var state by mutableStateOf(RecommendedViewState())
        private set

    init {
        getAllPlaces()
    }

    private fun getAllPlaces(){

        viewModelScope.launch (Dispatchers.IO){
            val allPlaces : List<Place> = dataLayerFacade.getAllPlaces()
            allPlaces.forEach {
                Log.d("DatosPlaces", "Place vm: $it")
            }
            state = state.copy(
                places = allPlaces,
                isLoading = false
            )
        }
    }

}

