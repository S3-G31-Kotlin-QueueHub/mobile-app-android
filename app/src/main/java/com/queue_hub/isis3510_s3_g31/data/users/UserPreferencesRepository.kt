package com.queue_hub.isis3510_s3_g31.data.users

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context, private val db: FirebaseFirestore) {
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

}