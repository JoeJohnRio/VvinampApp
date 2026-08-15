package com.example.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath

actual class DataStoreFactory(private val context: Context) {
    companion object {
        private var _instance: DataStore<Preferences>? = null
        private val lock = Any()

        private fun getInstance(context: Context): DataStore<Preferences> {
            val appContext = context.applicationContext
            return synchronized(lock) {
                _instance ?: PreferenceDataStoreFactory.createWithPath(
                    produceFile = {
                        appContext.filesDir.resolve(AUTH_PREFERENCES).absolutePath.toPath()
                    }
                ).also { _instance = it }
            }
        }
    }

    actual fun create(): DataStore<Preferences> {
        return getInstance(context)
    }
}
