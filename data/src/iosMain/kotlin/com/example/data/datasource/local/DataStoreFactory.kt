package com.example.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import okio.Path.Companion.toPath

actual class DataStoreFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun create(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                val directory = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null
                )
                (directory?.path + "/$AUTH_PREFERENCES").toPath()
            }
        )
    }
}
