package com.queue_hub.isis3510_s3_g31.ui.screens.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(private val dataLayerFacade: DataLayerFacade) : ViewModel() {


    val place = mutableStateOf(Place())
    val isLoading = mutableStateOf(true)
    val queuedState = mutableStateOf(false)
    val activeTurn = mutableStateOf(false)
    val onQueue = mutableStateOf(0)
    private val _isConnected = MutableStateFlow<Boolean>(false)
    val isConnected: StateFlow<Boolean> = _isConnected




    val lat = mutableStateOf(0.0)
    val lon =  mutableStateOf(0.0)

    init {
        checkInternetConnection()
        getPlace()
        getActiveTurn()
    }

    private fun getPlace() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            val fetchedPlace = dataLayerFacade.getPlaceToDetail() ?: Place()
            place.value = fetchedPlace
            val localization = place.value.localization
            try {
                val coordinates = localization.split(", ")
                lat.value = coordinates[0].trim().toDouble()
                lon.value = coordinates[1].trim().toDouble()



                println("Latitud: $lat, Longitud: $lon")
            } catch (e: Exception) {

                println("Error: Localization data is not in the expected format $localization\"")
                e.printStackTrace()
            }
            onQueue.value = dataLayerFacade.getTurnsNumberByUser(fetchedPlace.id)

            isLoading.value = false
        }
    }


    private fun getActiveTurn() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = dataLayerFacade.getIdUser()
            dataLayerFacade.getTurn(userId)
                .collect { turn ->
                    val isActive = turn.status in listOf("waiting", "active") && turn.idUser.isNotEmpty()
                    withContext(Dispatchers.Main) {
                        activeTurn.value = isActive
                    }
                }
        }
    }


    fun addTurn() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = dataLayerFacade.getIdUser()
            val success = dataLayerFacade.addTurn(userId.orEmpty(), place.value.id)
            queuedState.value = success
        }
    }
    private fun checkInternetConnection() {
        viewModelScope.launch {
            dataLayerFacade.checkNetworkConnection().collect { isConnected ->
                _isConnected.value = isConnected
            }
        }
    }

}

