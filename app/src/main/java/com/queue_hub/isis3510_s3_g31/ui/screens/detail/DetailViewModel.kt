package com.queue_hub.isis3510_s3_g31.ui.screens.detail

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.places.PlacesRepository
import com.queue_hub.isis3510_s3_g31.data.turns.TurnsRepository
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
class DetailViewModel (private val placesRepository: PlacesRepository, private val usersRepository: UsersRepository, private val turnsRepository: TurnsRepository): ViewModel() {

    var state by mutableStateOf(DetailViewState())
        private set
    var userId by mutableStateOf<String?>("null")
    private val _queued_state = MutableLiveData<Boolean>()
    val queued_state: LiveData<Boolean> = _queued_state

    init {
        viewModelScope.launch {
            userId = usersRepository.userId.first()
            state = state.copy(
                place = getPlace(), onQueue = getPeopleOnQueue()
            )
            getActiveTurn()
        };

    }
    suspend fun getActiveTurn() {
        _queued_state.value = false

    }

     suspend fun getPlace() : Place{

            return placesRepository.getPlace()
    }
    suspend fun getPeopleOnQueue() : Int{

        return turnsRepository.getTurnsLength(state.place.id)
    }

    suspend fun addTurn(){
        _queued_state.value = turnsRepository.addTurn(userId.orEmpty(), state.place.id)
    }
}

