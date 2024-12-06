package com.queue_hub.isis3510_s3_g31.utils.network_services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NetworkManager {
    val isConnected: StateFlow<Boolean>
}