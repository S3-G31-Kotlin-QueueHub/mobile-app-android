package com.queue_hub.isis3510_s3_g31.ui.screens.wait

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.queues.QueuesRepository
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.users.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WaitViewModel(
    private val turnsRepository: TurnsRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val queuesRepository: QueuesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<WaitViewState>(WaitViewState.Loading)
    val uiState: StateFlow<WaitViewState> = _uiState

    init {
        getTurn()
    }

    private fun cancelTurn(){

    }


    private fun getTurn(){
        viewModelScope.launch (Dispatchers.IO){
            val idUser = userPreferencesRepository.userId.first()

            turnsRepository.getTurn(idUser).collect{ turn ->
                val idPlace = turn.idPlace
                queuesRepository.getQueue(idPlace).collect{ queue ->
                    _uiState.value = WaitViewState.Success(turn, queue)
                }
            }
        }


    }
}