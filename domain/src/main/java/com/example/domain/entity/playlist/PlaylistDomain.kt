package com.example.domain.entity.playlist

import com.example.domain.entity.User
import com.example.domain.entity.album.SimplifiedTrackDomain
import com.example.domain.entity.song.TrackDomain

data class PlaylistDomain(
    val id: String,
    val title: String,
    val description: String?,
    val creator: User,
    val createdAt: String,
    val updatedAt: String?,
    val isPublic: Boolean,
    val coverImage: String?,
    val bannerImage: String?,
    val tracks: List<TrackDomain>,
    val savedCount: Int,
    val totalDuration: Int,
    val isSavedByMe: Boolean
)