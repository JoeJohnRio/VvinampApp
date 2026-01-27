package com.example.domain.entity.song

import kotlinx.serialization.Serializable

@Serializable
data class TrackDomain(
    val id: String,
    val title: String,
    val duration: Int,
    val streamCount: Int,
    val explicit: Boolean,
    val releaseDate: String,
    val audioFileUrl: String,
)