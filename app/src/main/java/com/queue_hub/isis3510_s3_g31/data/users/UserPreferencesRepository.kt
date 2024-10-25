package com.queue_hub.isis3510_s3_g31.data.users

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {
    companion object {
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
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


    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}