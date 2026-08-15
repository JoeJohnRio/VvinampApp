package com.example.vvinamp.util

import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set

class WasmAppSettings : AppSettings {
    override fun putString(key: String, value: String) {
        localStorage.set(key, value)
    }

    override fun getString(key: String, defaultValue: String): String {
        return localStorage.get(key) ?: defaultValue
    }

    override fun putBoolean(key: String, value: Boolean) {
        localStorage.set(key, value.toString())
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return localStorage.get(key)?.toBoolean() ?: defaultValue
    }

    override fun putInt(key: String, value: Int) {
        localStorage.set(key, value.toString())
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return localStorage.get(key)?.toIntOrNull() ?: defaultValue
    }

    override fun remove(key: String) {
        localStorage.removeItem(key)
    }

    override fun clear() {
        localStorage.clear()
    }
}
