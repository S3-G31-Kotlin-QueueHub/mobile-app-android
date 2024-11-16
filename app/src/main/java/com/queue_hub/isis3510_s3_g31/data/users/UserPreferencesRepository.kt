package com.queue_hub.isis3510_s3_g31.data.users

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.queue_hub.isis3510_s3_g31.data.users.model.User
import com.queue_hub.isis3510_s3_g31.ui.screens.signup.SignUpState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resumeWithException


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context, private val db: FirebaseFirestore, private val auth: FirebaseAuth) {
    companion object {
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        private val USER_NAME = stringPreferencesKey("name")
    }

    suspend fun saveUserData(email: String, userId: String) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
            preferences[USER_ID_KEY] = userId
            preferences[IS_LOGGED_IN_KEY] = true

        }
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN_KEY] ?: false
        }
    val userId: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY].toString()
        }
    val name: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME].toString()
        }
    val email: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[EMAIL_KEY].toString()
        }

    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun saveUserToken(token: String, idUser: String) {
        db.collection("users")
            .document(idUser)
            .update("token", token)
            .addOnSuccessListener {
                println("Token saved successfully")
            }
    }
    suspend fun getUserData( ) {
        try {
            val userDocument = db.collection("users").document(userId.first()).get().await()
            if (userDocument.exists()) {
                val userData = userDocument.data
                userData?.let {
                    context.dataStore.edit { preferences ->
                        val name = it["name"]?.toString()
                        if (name != null) {
                            preferences[USER_NAME] = name
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("Error getting user data: ${e.message}")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun signUp(email: String, password: String, phone: String, name: String) {

        // Convert Firebase Auth to coroutine-based call
        val authResult = suspendCancellableCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    continuation.resume(result, null)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        val userId = authResult.user?.uid ?: throw Exception("Failed to get user ID")

        val user = User(
            name = name,
            email = email,
            phone = phone,
            isAdmin = false,
            createdAt = Timestamp.now()
        )

        suspendCancellableCoroutine<Void> { continuation ->
            db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener { result ->
                    continuation.resume(result, null)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        saveUserData(email, userId)
    }
}