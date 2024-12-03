package com.queue_hub.isis3510_s3_g31.data.reviews

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.queue_hub.isis3510_s3_g31.data.reviews.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReviewsRepository(
    private val db: FirebaseFirestore
) {


    suspend fun getReviews(placeId: String): Flow<List<Review>> = callbackFlow {
        println("GET REVIEWS PLACE ID: $placeId")
        val reviewsRef = db.collection("reviews").document(placeId)

        println("REVIEWS REF: $reviewsRef")

        val reviewsSubscription = reviewsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                launch (Dispatchers.IO){
                    try {

                        val reviewsData = snapshot.data?.get("reviews") as? List<Map<String, Any>>

                        println("REVIEWS DATA: $reviewsData")
                        if(reviewsData == null){
                            trySend(emptyList())
                            return@launch
                        }

                        val reviews = coroutineScope {
                            reviewsData.map { reviewData ->
                                async {
                                    val idUser = reviewData["idUser"] as String
                                    val userDoc = db.collection("users").document(idUser).get().await()

                                    try {
                                        if(userDoc.exists()){
                                            Review(
                                                score = reviewData["score"] as Long,
                                                comment = reviewData["comment"] as String,
                                                userName = userDoc.getString("name") ?: "",
                                                date = reviewData["date"] as Timestamp
                                            )
                                        } else null
                                    } catch (e: Exception) {
                                        println("Error fetching user $idUser: ${e.message}")
                                        null
                                    }

                                }
                            }.awaitAll().filterNotNull()
                        }

                        val sortedReviews = reviews.sortedByDescending { it.date }
                        trySend(sortedReviews)

                    } catch (e: Exception) {
                        close(e)
                    }
                }
            }
        }
        awaitClose {
            reviewsSubscription.remove()
        }
    }

}