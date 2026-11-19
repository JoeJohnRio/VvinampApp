package com.example.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.w3c.dom.get
import org.w3c.dom.set

actual class DataStoreFactory {
    companion object {
        private val instance: DataStore<Preferences> by lazy { LocalStoragePreferences("$AUTH_PREFERENCES.") }
    }

    actual fun create(): DataStore<Preferences> = instance
}

/**
 * DataStore's own web storage is the tab's sessionStorage, so a login was lost in every new tab or window.
 * This keeps the preferences in localStorage instead, one entry per key under [prefix].
 * shortcut: only String preferences are stored, which is all the auth store uses.
 */
private class LocalStoragePreferences(private val prefix: String) : DataStore<Preferences> {
    private val mutex = Mutex()
    private val state = MutableStateFlow(read())

    override val data: Flow<Preferences> = state.asStateFlow()

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences =
        mutex.withLock {
            val updated = transform(state.value)
            write(updated)
            state.value = updated
            updated
        }

    private fun storedKeys(): List<String> =
        (0 until localStorage.length).mapNotNull { localStorage.key(it) }.filter { it.startsWith(prefix) }

    private fun read(): Preferences {
        val preferences = mutablePreferencesOf()
        storedKeys().forEach { key ->
            localStorage[key]?.let { preferences[stringPreferencesKey(key.removePrefix(prefix))] = it }
        }
        return preferences
    }

    private fun write(preferences: Preferences) {
        storedKeys().forEach { localStorage.removeItem(it) }
        preferences.asMap().forEach { (key, value) ->
            if (value is String) localStorage[prefix + key.name] = value
        }
    }
}
