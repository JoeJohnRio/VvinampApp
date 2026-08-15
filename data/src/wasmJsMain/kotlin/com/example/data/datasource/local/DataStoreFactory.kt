package com.example.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

actual class DataStoreFactory {
    companion object {
        private var _instance: DataStore<Preferences>? = null

        private fun getInstance(): DataStore<Preferences> {
            return _instance ?: PreferenceDataStoreFactory.createWithPath(
                produceFile = { AUTH_PREFERENCES.toPath() },
            ).also { _instance = it }
        }
    }

    actual fun create(): DataStore<Preferences> {
        return getInstance()
    }
}

