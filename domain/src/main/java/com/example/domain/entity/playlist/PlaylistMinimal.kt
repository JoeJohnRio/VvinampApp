package com.example.domain.entity.playlist

data class PlaylistMinimal(
    val id: String,
    val title: String,
    val coverImage: String?,
    val isSavedByMe: Boolean,
    val savedCount: Int,
    val username: String,
)