package com.queue_hub.isis3510_s3_g31.ui.screens.userQueues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.queues.QueuesRepository
import com.queue_hub.isis3510_s3_g31.data.queues.model.PreviousQueue
import com.queue_hub.isis3510_s3_g31.data.queues.model.Queue
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.turns.model.Turn
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserQueuesViewModel(
    private val dataLayerFacade: DataLayerFacade,
): ViewModel() {

    private val _queuesState = MutableStateFlow<UserQueuesViewState<List<PreviousQueue>>>(UserQueuesViewState.Loading())
    val queuesState: StateFlow<UserQueuesViewState<List<PreviousQueue>>> = _queuesState

    private val _turnState = MutableStateFlow<UserQueuesViewState<Turn>>(UserQueuesViewState.Loading())
    val turnState: StateFlow<UserQueuesViewState<Turn>> = _turnState

    private val _queueState = MutableStateFlow<UserQueuesViewState<Queue>>(UserQueuesViewState.Loading())
    val queueState: StateFlow<UserQueuesViewState<Queue>> = _queueState

    private val _showCancelDialog = MutableStateFlow(false)
    val showCancelDialog: StateFlow<Boolean> = _showCancelDialog


    init {
        getUserQueues()
        initializeTurnAndQueue()
    }

    private fun initializeTurnAndQueue() {
        viewModelScope.launch(Dispatchers.IO) {
            val idUser = dataLayerFacade.getIdUser()

            dataLayerFacade.getTurn(idUser).collect { turn ->
                _turnState.value = UserQueuesViewState.Success(turn)
                println("Turn Queues view: $turn")
                if(turn.idPlace.isNotEmpty()){
                    getQueue(turn.idPlace)
                } else {
                    _queueState.value = UserQueuesViewState.Success(Queue("", 0, ""))
                }
            }

        }
    }

    private fun getUserQueues(){
        viewModelScope.launch (Dispatchers.IO){
            val idUser = dataLayerFacade.getIdUser()

            dataLayerFacade.getPreviousUserQueues(idUser).collect{ queues ->
                _queuesState.value = UserQueuesViewState.Success(queues)
            }
        }
    }

    fun cancelTurn(){

        viewModelScope.launch(Dispatchers.IO) {
            hideCancelDialog()
            val idUser = dataLayerFacade.getIdUser()
            dataLayerFacade.cancelTurn(idUser)
        }
    }

    private fun getQueue(idQueue: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataLayerFacade.getQueue(idQueue).collect { queue ->
                _queueState.value = UserQueuesViewState.Success(queue)
            }
        }
    }

    fun showCancelDialog() {
        _showCancelDialog.value = true
    }

    fun hideCancelDialog() {
        _showCancelDialog.value = false
    }

}