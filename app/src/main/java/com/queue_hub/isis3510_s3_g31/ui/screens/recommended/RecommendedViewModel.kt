package com.queue_hub.isis3510_s3_g31.ui.screens.recommended

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
            val n = allPlaces.size
            var place: Place? = null
            for (i in 0 until n) {
                place = allPlaces[i]
                Log.d("DatosPlaces", "Place vm: $place")
            }
            state = state.copy(
                places = allPlaces,
                isLoading = false,
                selectedCategory = RecommendedCategory.ALL
            )
        }
    }

     fun uploadPlace(placeId:String){
        viewModelScope.launch (Dispatchers.IO) {
            dataLayerFacade.setPlaceToDetail(placeId)
        }
    }

    private fun getLessWaitingTimeLastHour(){

        viewModelScope.launch (Dispatchers.IO){
            val lessWaitingTime : List<Place> = dataLayerFacade.getLessWaitingTimeLastHour()
            state = state.copy(
                places = lessWaitingTime,
                isLoading = false,
                selectedCategory = RecommendedCategory.LESSWAITINGTIMELASTHOUR
            )
        }
    }

    fun updateSelectedCategory( recommendedCategory: RecommendedCategory ){

        state = state.copy(
            isLoading = true,
            selectedCategory = recommendedCategory
        )

        viewModelScope.launch (Dispatchers.Default){

            when (recommendedCategory) {
                RecommendedCategory.ALL -> getAllPlaces()
                RecommendedCategory.LESSWAITINGTIMELASTHOUR -> getLessWaitingTimeLastHour()
            }

        }
    }

}

