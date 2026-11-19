package com.example.domain.entity.playlist

enum class PlaylistTrackSortByDomain(val label: String) {
    CUSTOM("Custom order"),
    TITLE("Title"),
    ARTIST_NAME("Artist"),
    ALBUM_NAME("Album"),
    RECENTLY_ADDED("Recently added")
}
