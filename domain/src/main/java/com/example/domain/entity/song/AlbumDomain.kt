package com.example.domain.entity.song

import kotlinx.serialization.Serializable

@Serializable
data class AlbumDomain(
    val id: String,
    val title: String,
    val releaseDate: String,
    val coverArt: String?,
)