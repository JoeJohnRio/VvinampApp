package com.example.vvinamp.util

import platform.Foundation.NSUserDefaults

class IosAppSettings : AppSettings {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    override fun putString(key: String, value: String) {
        userDefaults.setObject(value, key)
    }

    override fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }

    override fun putBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, key)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return if (userDefaults.objectForKey(key) == null) defaultValue else userDefaults.boolForKey(key)
    }

    override fun putInt(key: String, value: Int) {
        userDefaults.setInteger(value.toLong(), key)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return if (userDefaults.objectForKey(key) == null) defaultValue else userDefaults.integerForKey(key).toInt()
    }

    override fun remove(key: String) {
        userDefaults.removeObjectForKey(key)
    }

    override fun clear() {
        val dictionary = userDefaults.dictionaryRepresentation()
        dictionary.keys.forEach { key ->
            val keyString = key as? String
            if (keyString != null) {
                userDefaults.removeObjectForKey(keyString)
            }
        }
    }
}
