package com.example.presentation.entities

sealed class MusicListItem {
    data class Music(
        val id: Int,
        val imageUrl: String,
        val category: String,
    ) : MusicListItem()

    data class Separator(val category: String) : MusicListItem()
}
