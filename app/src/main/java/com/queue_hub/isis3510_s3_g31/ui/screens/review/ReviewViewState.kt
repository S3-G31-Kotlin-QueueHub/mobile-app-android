package com.queue_hub.isis3510_s3_g31.ui.screens.review

sealed class ReviewViewState<out T> {
    data class Success<T>(val data: T) : ReviewViewState<T>()
    data class Loading<T>(val previousData: T? = null) : ReviewViewState<T>()
    data class Error<T>(val error: Throwable, val previousData: T? = null) : ReviewViewState<T>()
}