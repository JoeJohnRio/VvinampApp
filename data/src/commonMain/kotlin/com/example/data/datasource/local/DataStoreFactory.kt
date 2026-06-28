package com.example.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class DataStoreFactory {
    fun create(): DataStore<Preferences>
}

internal const val AUTH_PREFERENCES = "auth_preferences.preferences_pb"
