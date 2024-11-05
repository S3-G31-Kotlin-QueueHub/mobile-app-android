package com.queue_hub.isis3510_s3_g31.data.turns

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.queue_hub.isis3510_s3_g31.data.turns.local.TurnDao
import com.queue_hub.isis3510_s3_g31.data.turns.local.entity.TurnEntity
import com.queue_hub.isis3510_s3_g31.data.turns.model.EndedTurn
import com.queue_hub.isis3510_s3_g31.data.turns.remote.TurnApi
import com.queue_hub.isis3510_s3_g31.data.turns.model.Turn
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class TurnsRepository(
    private val turnsApi : TurnApi,
    private val db: FirebaseFirestore,
    private val turnsDao: TurnDao
) {

    suspend fun cancelTurn(idUser: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            db.collection("turns")
                .whereEqualTo("idUser", idUser)
                .whereEqualTo("status", "waiting")
                .limit(1)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        continuation.resume(false)
                        return@addOnSuccessListener
                    }
                    val turnDocument = querySnapshot.documents[0]
                    db.collection("turns")
                        .document(turnDocument.id)
                        .update(
                            mapOf(
                                "status" to "cancelled",
                                "cancelledAt" to FieldValue.serverTimestamp()
                            )
                        )
                }
                .addOnFailureListener { e ->
                    continuation.resume(false)
                }



        }
    }

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



    suspend fun getAllTurnsOfUser(idUser: String): Flow<List<EndedTurn>> = callbackFlow {
        val turnRef = db.collection("turns")
            .whereEqualTo("idUser", idUser)


        val turnSubscription = turnRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }


            if (snapshot != null) {
                val turnsList = mutableListOf<EndedTurn>()
                try {

                    if (!snapshot.isEmpty) {
                        for (document in snapshot.documents) {
                            val turnData = document.data
                            val turn = EndedTurn(
                                idUser = turnData?.get("idUser") as? String ?: "",
                                idPlace = turnData?.get("idPlace") as? String ?: "",
                                createdAt = turnData?.get("createdAt") as? Timestamp ?: Timestamp.now(),
                                endedAt = turnData?.get("endedAt") as? Timestamp ?: Timestamp.now(),
                                way = turnData?.get("way") as? String ?: "",
                                status = turnData?.get("status") as? String ?: "",
                                turnNumber = turnData?.get("turnNumber") as? Long ?: 0
                            )
                            turnsList.add(turn)
                        }

                        trySend(turnsList)
                    } else {

                        trySend(emptyList())
                    }
                } catch (e: Exception) {
                    close(e)
                }
            }
        }


        awaitClose {
            turnSubscription.remove()
        }
    }

    suspend fun fetchAndSaveTurnsForUser(userId: String) {

        val turnsFlow = getAllTurnsOfUser(userId)

        turnsFlow.collect { turnsList ->

            turnsDao.insertTurns(turnsList.map { turn ->
                TurnEntity(
                    idUser = turn.idUser,
                    idPlace = turn.idPlace,
                    createdAt = turn.createdAt.toString(),
                    endedAt = turn.endedAt.toString(),
                    way = turn.way,
                    status = turn.status,
                    turnNumber = turn.turnNumber
                )
            })
        }
    }

}