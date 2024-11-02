package com.queue_hub.isis3510_s3_g31.data.users.model

import com.google.firebase.Timestamp

data class User(
    val name: String,
    val email: String,
    val phone: String,
    val isAdmin: Boolean,
    val createdAt: Timestamp,
)