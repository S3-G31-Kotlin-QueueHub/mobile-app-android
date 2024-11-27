package com.queue_hub.isis3510_s3_g31.utils.network_services

import kotlinx.coroutines.flow.Flow

interface NetworkManager {
    val isConnected: Flow<Boolean>
}