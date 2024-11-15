package com.queue_hub.isis3510_s3_g31.ui.screens.userQueues

sealed class UserQueuesViewState<out T> {
    data class Success<T>(val data: T) : UserQueuesViewState<T>()
    data class Loading<T>(val previousData: T? = null) : UserQueuesViewState<T>()
    data class Error<T>(val error: Throwable, val previousData: T? = null) : UserQueuesViewState<T>()
}

