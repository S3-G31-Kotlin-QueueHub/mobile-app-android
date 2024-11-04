package com.queue_hub.isis3510_s3_g31.ui.screens.wait

import com.queue_hub.isis3510_s3_g31.data.queues.model.Queue
import com.queue_hub.isis3510_s3_g31.data.turns.model.Turn

sealed class WaitViewState {

    object Loading : WaitViewState()
    data class Success(val turn: Turn, val queue: Queue) : WaitViewState()
    data class Error(val message: String) : WaitViewState()

}