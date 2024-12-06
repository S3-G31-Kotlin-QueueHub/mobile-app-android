package com.queue_hub.isis3510_s3_g31.utils.network_services

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class AndroidNetworkManager(context: Context) : NetworkManager {

    private val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> get() = _isConnected

    init {
        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isConnected.value = true
            }

            override fun onLost(network: Network) {
                _isConnected.value = false
            }
        }

        // Registrar el callback solo una vez
        connectivityManager.registerDefaultNetworkCallback(callback)
    }
}
