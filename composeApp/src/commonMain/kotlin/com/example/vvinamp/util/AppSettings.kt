package com.example.vvinamp.util

interface AppSettings {
    fun putString(key: String, value: String)
    fun getString(key: String, defaultValue: String): String
    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun putInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int): Int
    fun remove(key: String)
    fun clear()
}
