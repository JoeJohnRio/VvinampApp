package com.example.presentation.ui.home.library.detailplaylist.editplaylist

import android.os.Parcelable

data class PlaylistEditUiState(
    val playlistId: String = "",
    val editedSong: List<EditedPlaylistTrack> = emptyList(),
    val deletedTrackId: List<String> = emptyList(),
//    val isMove
) {
    fun isSaveEnable(): Boolean {
        return editedSong.isNotEmpty() && deletedTrackId.isNotEmpty()
    }
}