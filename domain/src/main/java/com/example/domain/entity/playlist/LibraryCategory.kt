package com.example.domain.entity.playlist

import kotlinx.serialization.Serializable

@Serializable
enum class LibraryCategory(val displayName: String) {
    ALL("All"),
    PLAYLIST("Playlists"),
    PODCAST("Podcasts"),
    ALBUM("Albums"),
    ARTIST("Artists"),
    DOWNLOADED("Downloaded");

    companion object {
        fun String.toLibraryCategory(): LibraryCategory {
            return LibraryCategory.entries.firstOrNull { it.displayName.equals(this, ignoreCase = true) }
                ?: ALL
        }
    }
}