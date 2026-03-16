package com.example.presentation.ui.home.library.detailplaylist

sealed class PlaylistUiEvent {
    object onClickChange: PlaylistUiEvent()
    object onClickShare: PlaylistUiEvent()
    object onClickDetail: PlaylistUiEvent()
}