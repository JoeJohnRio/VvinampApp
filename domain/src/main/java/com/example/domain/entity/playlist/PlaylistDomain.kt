package com.example.domain.entity.playlist

import com.example.domain.entity.album.SimplifiedTrackDomain

data class PlaylistDomain(
    val id: String,
    val title: String,
    val artistName: String,
    val artistImage: String,
    val releaseDate: String,
    val coverArt: String?,
    val tracks: List<SimplifiedTrackDomain>,
    val trackCount: Int,
    val totalDuration: Int,
)