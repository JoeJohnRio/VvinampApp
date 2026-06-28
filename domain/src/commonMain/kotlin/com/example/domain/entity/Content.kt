package com.example.domain.entity

data class Content(
    // Common fields
    val id: String,
    val title: String,
    val imageUrl: String? = null,

    // Track-specific fields
    val artistName: String? = null,
    val albumName: String? = null,
    val durationMs: Int? = null,

    // Podcast-specific fields
    val publisherName: String? = null,
    val description: String? = null,

    // Playlist-specific fields
    val ownerName: String? = null
)