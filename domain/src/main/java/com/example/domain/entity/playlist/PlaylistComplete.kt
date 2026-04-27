package com.example.domain.entity.playlist

import com.example.domain.entity.user.User
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.user.UserMinimal

data class PlaylistComplete(
    val id: String,
    val title: String,
    val description: String?,
    val creator: UserMinimal,
    val createdAt: String,
    val updatedAt: String?,
    val isPublic: Boolean,
    val coverImage: String?,
    val albumCovers: List<String>,
    val savedCount: Int,
    val totalDuration: Int,
    val isSavedByMe: Boolean
)