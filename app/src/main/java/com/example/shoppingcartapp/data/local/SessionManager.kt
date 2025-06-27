package com.example.shoppingcartapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val JWT_TOKEN = stringPreferencesKey("jwt_token")
        val USER_ROLE = stringPreferencesKey("user_role")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_USERNAME = stringPreferencesKey("user_username")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[JWT_TOKEN] = token }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { it[JWT_TOKEN] }.first()
    }

    suspend fun clearToken() {
        context.dataStore.edit { it.remove(JWT_TOKEN) }
    }

    suspend fun saveRole(role: String) {
        context.dataStore.edit { it[USER_ROLE] = role }
    }

    suspend fun getRole(): String? {
        return context.dataStore.data.map { it[USER_ROLE] }.first()
    }

    suspend fun saveUsername(role: String) {
        context.dataStore.edit { it[USER_USERNAME] = role }
    }

    suspend fun getUsername(): String? {
        return context.dataStore.data.map { it[USER_USERNAME] }.first()
    }

    suspend fun saveName(role: String) {
        context.dataStore.edit { it[USER_NAME] = role }
    }

    suspend fun getName(): String? {
        return context.dataStore.data.map { it[USER_NAME] }.first()
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
