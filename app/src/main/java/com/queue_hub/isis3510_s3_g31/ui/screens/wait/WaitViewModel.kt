package com.queue_hub.isis3510_s3_g31.ui.screens.wait

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.queues.QueuesRepository
import com.queue_hub.isis3510_s3_g31.data.queues.model.Queue
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import com.queue_hub.isis3510_s3_g31.ui.screens.userQueues.UserQueuesViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WaitViewModel(
    private val dataLayerFacade: DataLayerFacade,
): ViewModel() {

    private val _uiState = MutableStateFlow<WaitViewState>(WaitViewState.Loading)
    val uiState: StateFlow<WaitViewState> = _uiState

    private val _isConnected = MutableStateFlow<Boolean>(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    init {
        getTurn()
        checkInternetConnection()
    }

    fun cancelTurn(){
        viewModelScope.launch (Dispatchers.IO){
            val idUser = dataLayerFacade.userId().first()
            dataLayerFacade.cancelTurn(idUser)
        }
    }

    private fun getTurn(){
        viewModelScope.launch (Dispatchers.IO){
            val idUser = dataLayerFacade.userId().first()

            dataLayerFacade.getTurn(idUser).collect{ turn ->

                if(turn.idPlace.isNotEmpty()){
                    dataLayerFacade.getQueue(turn.idPlace).collect{ queue ->
                        _uiState.value = WaitViewState.Success(turn, queue)
                    }
                }
            }
        }
    }

    private fun checkInternetConnection() {
        viewModelScope.launch (Dispatchers.IO) {
            dataLayerFacade.checkNetworkConnection().collect { isConnected ->
                _isConnected.value = isConnected
            }
        }
    }
}