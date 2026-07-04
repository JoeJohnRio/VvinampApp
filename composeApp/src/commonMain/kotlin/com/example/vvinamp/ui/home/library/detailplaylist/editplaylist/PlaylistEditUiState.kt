package com.example.vvinamp.ui.home.library.detailplaylist.editplaylist

data class PlaylistEditUiState(
    val playlistId: String = "",
    val editedSong: List<EditedPlaylistTrack> = emptyList(),
    val deletedTrackId: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    fun isSaveEnable(): Boolean {
        return editedSong.isNotEmpty() || deletedTrackId.isNotEmpty()
    }
}
