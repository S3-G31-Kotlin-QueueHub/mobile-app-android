package com.queue_hub.isis3510_s3_g31.data.turns

import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.queue_hub.isis3510_s3_g31.data.turns.remote.TurnApi
import com.queue_hub.isis3510_s3_g31.data.users.remote.LoginRequest
import com.queue_hub.isis3510_s3_g31.data.users.remote.model.Turn
import com.queue_hub.isis3510_s3_g31.data.users.remote.model.User
import com.queue_hub.isis3510_s3_g31.ui.screens.signup.SignUpState
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume


class TurnsRepository(
    private val turnsApi : TurnApi,
    private val db: FirebaseFirestore
) {

    suspend fun addTurn(uid: String, ruid: String): Boolean {
        val turn = Turn(
            id = UUID.randomUUID().toString(),
            userId = uid,
            restaurantId = ruid,
            createdAt = Timestamp.now()
        )

        val db = FirebaseFirestore.getInstance()

        return suspendCancellableCoroutine { continuation ->
            db.collection("users")
                .document(uid)
                .collection("turns")
                .add(turn)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener { e ->
                    continuation.resume(false)
                }
        }
    }

}