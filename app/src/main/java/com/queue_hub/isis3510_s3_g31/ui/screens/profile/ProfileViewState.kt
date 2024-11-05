package com.queue_hub.isis3510_s3_g31.ui.screens.profile

import com.queue_hub.isis3510_s3_g31.data.places.model.Place
import com.queue_hub.isis3510_s3_g31.data.turns.model.EndedTurn

data class ProfileViewState(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val turns: List<EndedTurn> = emptyList()
)
