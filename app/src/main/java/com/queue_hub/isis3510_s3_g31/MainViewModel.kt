package com.queue_hub.isis3510_s3_g31

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.queue_hub.isis3510_s3_g31.data.users.UserPreferencesRepository
import com.queue_hub.isis3510_s3_g31.ui.navigation.Login
import com.queue_hub.isis3510_s3_g31.ui.navigation.Main
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainViewModel: ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<Any>(Login)
    val startDestination: StateFlow<Any> = _startDestination.asStateFlow()

    fun checkAuthState(userPreferencesRepository: UserPreferencesRepository) {
        viewModelScope.launch {
            userPreferencesRepository.isLoggedIn.collect { isLoggedIn ->



                if(isLoggedIn){
                    tokenNew(userPreferencesRepository)

                    _startDestination.value = Main
                } else{
                    _startDestination.value = Login
                }
                delay(1000L)
                _isLoading.value = false
            }
        }
    }

    private fun tokenNew(userPreferencesRepository: UserPreferencesRepository) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM TOKEN", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            viewModelScope.launch {
                saveUserToken(token, userPreferencesRepository)
            }
            Log.d("FCM TOKEN", token.toString())
        })
    }

    private suspend fun saveUserToken(token: String, userPreferencesRepository: UserPreferencesRepository) {
        val idUser = userPreferencesRepository.userId.first()
        viewModelScope.launch (Dispatchers.IO) {
            userPreferencesRepository.saveUserToken(token, idUser)
        }
    }
}