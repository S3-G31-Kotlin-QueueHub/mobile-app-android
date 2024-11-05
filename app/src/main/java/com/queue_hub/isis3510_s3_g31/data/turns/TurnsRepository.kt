package com.queue_hub.isis3510_s3_g31.data.turns

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.queue_hub.isis3510_s3_g31.data.turns.remote.TurnApi
import com.queue_hub.isis3510_s3_g31.data.turns.model.Turn
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume


class TurnsRepository(
    private val turnsApi : TurnApi,
    private val db: FirebaseFirestore
) {


    suspend fun getTurn(idUser: String): Flow<Turn> = callbackFlow {
        val turnRef = db.collection("turns").whereEqualTo("idUser", idUser).whereIn("status", listOf("waiting", "active")).limit(1)

        val turnSubscription = turnRef.addSnapshotListener { snapshot, error ->

            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                try {

                    if (snapshot.isEmpty) {
                        return@addSnapshotListener
                    }

                    val turnData = snapshot?.documents?.get(0)?.data
                    val turn = Turn(
                        idUser = turnData?.get("idUser") as? String ?: "",
                        idPlace = turnData?.get("idPlace") as? String ?: "",
                        createdAt = turnData?.get("createdAt") as? Timestamp ?: Timestamp.now(),
                        way = turnData?.get("way") as? String ?: "",
                        status = turnData?.get("status") as? String ?: "",
                        turnNumber = turnData?.get("turnNumber") as? Long ?: 0
                    )
                    println("Turn: $turn")
                    trySend(turn)
                } catch (e: Exception) {
                    close(e)
                }
            }
        }
        awaitClose {
            turnSubscription.remove()
        }
    }

    suspend fun addTurn(uid: String, ruid: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            db.collection("queues")
                .document(ruid)
                .get()
                .addOnSuccessListener { queueSnapshot ->
                    val queueData = queueSnapshot.data
                    println("Queue data: $queueData")
                    val lastTurnNumber = queueData?.get("lastTurnNumber") as? Long ?: 0
                    println("Last turn number: $lastTurnNumber")
                    val nextTurnNumber = lastTurnNumber + 1

                    val turn = Turn(
                        idUser = uid,
                        idPlace = ruid,
                        createdAt = Timestamp.now(),
                        way = "app",
                        status = "waiting",
                        turnNumber = nextTurnNumber
                    )

                    db.collection("turns")
                        .add(turn)
                        .addOnSuccessListener { turnRef ->
                            val idTurn = turnRef.id
                            val queueUpdate = hashMapOf(
                                "turns" to FieldValue.arrayUnion(idTurn),
                                "lastTurnNumber" to nextTurnNumber,
                                "lastUpdatedAt" to Timestamp.now()
                            )

                            db.collection("queues")
                                .document(ruid)
                                .update(queueUpdate)
                                .addOnSuccessListener {
                                    continuation.resume(true)
                                }
                                .addOnFailureListener { e ->
                                    continuation.resume(false)
                                }
                        }
                        .addOnFailureListener { e ->
                            continuation.resume(false)
                        }
                }
                .addOnFailureListener { e ->
                    continuation.resume(false)
                }
        }
    }
    suspend fun getTurnsLength(placeId: String): Int {
        var turnsCount = 0





        return turnsCount
    }


}