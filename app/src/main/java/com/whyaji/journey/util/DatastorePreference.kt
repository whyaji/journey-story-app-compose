package com.whyaji.journey.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class DatastorePreference private constructor(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
    private val prefs = context.dataStore

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")

        @Volatile
        private var instance: DatastorePreference? = null

        fun getInstance(context: Context): DatastorePreference {
            return instance ?: synchronized(this) {
                instance ?: DatastorePreference(context.applicationContext).also { instance = it }
            }
        }
    }

    val tokenFlow: Flow<String?>
        get() = prefs.data.catch { exception ->
            // Handle exception here, such as IOException
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[TOKEN_KEY]
        }

    suspend fun getToken(): String? {
        val preferences = prefs.data.first()
        return preferences[TOKEN_KEY]
    }

    suspend fun setToken(token: String) {
        prefs.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }
}
