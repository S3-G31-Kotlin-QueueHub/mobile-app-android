package com.queue_hub.isis3510_s3_g31.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationData
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val dataLayerFacade: DataLayerFacade,
    private val locationProvider: LocationProvider
): ViewModel(){


    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val uiState: StateFlow<HomeViewState> = _uiState
    private val _locationData = MutableStateFlow<LocationData?>(null)
    val locationData: StateFlow<LocationData?> get() = _locationData

    init {
        getCommonPlaces()
        startLocationUpdates()

    }

    private fun getCommonPlaces(){

        viewModelScope.launch (Dispatchers.IO){
            val idUser = dataLayerFacade.getIdUser()
            println("idUser: $idUser")

            dataLayerFacade.getCommonPlaces(idUser).collect{ commonPlaces ->
                _uiState.value = HomeViewState.Success(commonPlaces, null)
            }
        }
    }
    private fun startLocationUpdates() {
        viewModelScope.launch {

            locationProvider.requestLocationUpdates().collect { location ->
                _locationData.value = location
            }
        }
    }

    fun setPlace(place: Place) {

        viewModelScope.launch (Dispatchers.IO){
            dataLayerFacade.setPlaceToDetail(place)
        }

    }
}