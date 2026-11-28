package com.example.vvinamp.ui.main.playback

/**
 * Where the current queue came from, shown at the top of the player, e.g. "PLAYLIST" and its title.
 * [id] is the playlist, album or artist to open when that line is tapped; null when there's no page for it.
 */
data class PlayingFrom(val label: String, val name: String, val id: String? = null) {
    companion object {
        const val PLAYLIST = "PLAYLIST"
        const val ALBUM = "ALBUM"
        const val ARTIST = "ARTIST"
    }
}
