package com.example.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.data.repository.auth.AuthStore
import com.example.domain.entity.AuthSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreAuthStore(
    private val dataStore: DataStore<Preferences>
) : AuthStore {

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val EXPIRES_AT = stringPreferencesKey("expires_at")
        val USER_ID = stringPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
        val PROFILE_PICTURE = stringPreferencesKey("profile_picture")
    }

    override suspend fun saveTokens(access: String, refresh: String?, expiresAt: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = access
            refresh?.let { preferences[REFRESH_TOKEN] = it }
            preferences[EXPIRES_AT] = expiresAt
        }
    }

    override suspend fun saveUser(
        userId: String,
        username: String,
        email: String,
        profilePicture: String?
    ) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USERNAME] = username
            preferences[EMAIL] = email
            profilePicture?.let { preferences[PROFILE_PICTURE] = it }
        }
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.first()[ACCESS_TOKEN]
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[REFRESH_TOKEN]
    }

    override suspend fun getExpiresAt(): String? {
        return dataStore.data.first()[EXPIRES_AT]
    }

    override fun getSession(): Flow<AuthSession?> {
        return dataStore.data.map { preferences ->
            val access = preferences[ACCESS_TOKEN]
            val refresh = preferences[REFRESH_TOKEN]
            val expiresAt = preferences[EXPIRES_AT]
            val userId = preferences[USER_ID]
            val username = preferences[USERNAME]
            val email = preferences[EMAIL]
            val profilePicture = preferences[PROFILE_PICTURE]

            if (access != null && expiresAt != null) {
                AuthSession(
                    accessToken = access,
                    refreshToken = refresh,
                    expiresAt = expiresAt,
                    userId = userId,
                    username = username,
                    email = email,
                    profilePicture = profilePicture
                )
            } else null
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
            preferences.remove(EXPIRES_AT)
            preferences.remove(USER_ID)
            preferences.remove(USERNAME)
            preferences.remove(EMAIL)
            preferences.remove(PROFILE_PICTURE)
        }
    }
}
