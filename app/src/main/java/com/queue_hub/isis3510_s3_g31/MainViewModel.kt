package com.queue_hub.isis3510_s3_g31

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queue_hub.isis3510_s3_g31.data.users.UserPreferencesRepository
import com.queue_hub.isis3510_s3_g31.ui.navigation.Login
import com.queue_hub.isis3510_s3_g31.ui.navigation.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel: ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<Any>(Login)
    val startDestination: StateFlow<Any> = _startDestination.asStateFlow()

    fun checkAuthState(userPreferencesRepository: UserPreferencesRepository) {
        viewModelScope.launch {
            userPreferencesRepository.isLoggedIn.collect { isLoggedIn ->
                _startDestination.value = if (isLoggedIn) Main else Login
                delay(1000L)
                _isLoading.value = false
            }
        }
    }
}