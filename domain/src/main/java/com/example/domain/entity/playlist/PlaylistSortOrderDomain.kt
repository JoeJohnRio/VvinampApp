package com.example.domain.entity.playlist

enum class PlaylistSortOrderDomain(val label: String) {
    RECENTS("Recents"),
    RECENTLY_ADDED("Recently Added"),
    ALPHABETICAL("Alphabetical"),
    CREATOR("Creator"),
    UNKNOWN("Unknown"); // Fixed naming convention

    companion object {
        // Safety helper to prevent crashes when parsing API data
        fun fromString(value: String?): PlaylistSortOrderDomain {
            return entries.find { it.name == value } ?: UNKNOWN
        }
    }
}