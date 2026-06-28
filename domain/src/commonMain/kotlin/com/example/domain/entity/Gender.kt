package com.example.domain.entity

import kotlinx.serialization.Serializable

@Serializable
enum class Gender(val displayName: String) {
    FEMALE("Female"),
    MALE("Male"),
    NON_BINARY("Non-Binary"),
    OTHER("Other"),
    PREFER_NOT_TO_SAY("Prefer not to say");

    companion object {
        fun fromDisplayName(name: String): Gender? {
            return entries.firstOrNull { it.displayName.equals(name, ignoreCase = true) }
        }
    }
}