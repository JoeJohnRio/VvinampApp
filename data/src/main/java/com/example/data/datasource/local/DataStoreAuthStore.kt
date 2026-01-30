package com.example.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.repository.auth.AuthStore
import com.example.domain.entity.AuthSession
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreAuthStore @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthStore {

    private val Context.dataStore by preferencesDataStore("auth_preferences")

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val EXPIRES_AT = stringPreferencesKey("expires_at")
    }

    override suspend fun saveTokens(access: String, refresh: String?, expiresAt: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = access + "joel123"
            refresh?.let { preferences[REFRESH_TOKEN] = it }
            preferences[EXPIRES_AT] = expiresAt
        }
    }

    override suspend fun getAccessToken(): String? {
        return context.dataStore.data.first()[ACCESS_TOKEN]
    }

    override suspend fun getRefreshToken(): String? {
        return context.dataStore.data.first()[REFRESH_TOKEN]
    }

    override suspend fun getExpiresAt(): String? {
        return context.dataStore.data.first()[EXPIRES_AT]
    }

    override fun getSession(): Flow<AuthSession?> {
        return context.dataStore.data.map { preferences ->
            val access = preferences[ACCESS_TOKEN]
            val refresh = preferences[REFRESH_TOKEN]
            val expiresAt = preferences[EXPIRES_AT]

            if (access != null && expiresAt != null) {
                AuthSession(access, refresh, expiresAt)
            } else null
        }
    }

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
        }
    }
}