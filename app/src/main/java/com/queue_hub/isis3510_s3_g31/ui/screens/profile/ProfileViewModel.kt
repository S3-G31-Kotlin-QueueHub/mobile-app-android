package com.queue_hub.isis3510_s3_g31.ui.screens.profile

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.DataLayerFacade
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.turns.model.EndedTurn
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
class ProfileViewModel (private val placesRepository: PlacesRepository, private val usersRepository: UsersRepository, private val turnsRepository: TurnsRepository,private val dataLayerFacade: DataLayerFacade ): ViewModel() {

    var state by mutableStateOf(ProfileViewState())
        private set
    private val _isConnected = MutableStateFlow<Boolean>(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    var turns  =  mutableStateOf(0)


    init {
        checkInternetConnection()
        viewModelScope.launch {
            usersRepository.getUserData();

            state = state.copy(
                id = usersRepository.userId.first(),
                name = usersRepository.name.first(),
                email = usersRepository.email.first(),

            )
            getTurnsByUser()
        }
    }



    suspend fun logOut(){
        usersRepository.clearUserData()
    }

    fun getTurnsByUser(){

        viewModelScope.launch(Dispatchers.IO) {
            println("BUSCARE PARA EL USUARIO: " + state.id)
            turnsRepository.getAllTurnsOfUser(state.id).collect { list ->

                turns.value = list.size;
            }
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

