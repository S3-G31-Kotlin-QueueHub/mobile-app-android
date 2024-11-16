package com.queue_hub.isis3510_s3_g31.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(
    private val dataLayerFacade: DataLayerFacade
): ViewModel(){


    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val uiState: StateFlow<HomeViewState> = _uiState

    init {
        getCommonPlaces()
    }

    private fun getCommonPlaces(){

        viewModelScope.launch (Dispatchers.IO){
            val idUser = dataLayerFacade.getIdUser()
            println("idUser: $idUser")

            dataLayerFacade.getCommonPlaces(idUser).collect{ commonPlaces ->
                _uiState.value = HomeViewState.Success(commonPlaces)
            }
        }
    }

    fun setPlace(place: Place) {

        viewModelScope.launch (Dispatchers.IO){
            dataLayerFacade.setPlaceToDetail(place)
        }

    }
}