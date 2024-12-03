package com.queue_hub.isis3510_s3_g31

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.queue_hub.isis3510_s3_g31.data.users.UsersRepository
import com.queue_hub.isis3510_s3_g31.ui.navigation.Login
import com.queue_hub.isis3510_s3_g31.ui.navigation.Main
import com.queue_hub.isis3510_s3_g31.utils.location_services.LocationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainViewModel: ViewModel() {

    private val _location = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<Any>(Login)
    val startDestination: StateFlow<Any> = _startDestination.asStateFlow()

    fun updateLocation(newLocation : LocationData){
        _location.value = newLocation
    }

    fun checkAuthState(usersRepository: UsersRepository) {
        viewModelScope.launch {
            usersRepository.isLoggedIn.collect { isLoggedIn ->



                if(isLoggedIn){
                    tokenNew(usersRepository)

                    _startDestination.value = Main
                } else{
                    _startDestination.value = Login
                }
                delay(1000L)
                _isLoading.value = false
            }
        }
    }

    private fun tokenNew(usersRepository: UsersRepository) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM TOKEN", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            viewModelScope.launch {
                saveUserToken(token, usersRepository)
            }
            Log.d("FCM TOKEN", token.toString())
        })
    }

    private suspend fun saveUserToken(token: String, usersRepository: UsersRepository) {
        val idUser = usersRepository.userId.first()
        viewModelScope.launch (Dispatchers.IO) {
            usersRepository.saveUserToken(token, idUser)
        }
    }
}