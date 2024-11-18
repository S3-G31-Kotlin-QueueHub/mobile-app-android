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

class DetailViewModel(private val dataLayerFacade: DataLayerFacade) : ViewModel() {


    val place = mutableStateOf(Place())
    val isLoading = mutableStateOf(true)
    val queuedState = mutableStateOf(false)
    val onQueue = mutableStateOf(0)

    init {
        getPlace()
    }

    private fun getPlace() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true // Activa la carga al iniciar la operación
            val fetchedPlace = dataLayerFacade.getPlaceToDetail() ?: Place()
            place.value = fetchedPlace

            // Actualizar información adicional
            onQueue.value = dataLayerFacade.getTurnsNumberByUser(fetchedPlace.id)

            isLoading.value = false // Desactiva la carga al finalizar
        }
    }


    //    private  fun getActiveTurn() {
//        _queuedState.value = true
//    }
//
    fun addTurn() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = dataLayerFacade.getIdUser()
            val success = dataLayerFacade.addTurn(userId.orEmpty(), place.value.id)
            queuedState.value = success
        }
    }
}

