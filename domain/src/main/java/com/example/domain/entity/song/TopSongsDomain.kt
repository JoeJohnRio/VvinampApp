package com.example.domain.entity.song

import kotlinx.serialization.Serializable


@Serializable
data class TopSongsDomain(
    val success: Boolean,
    val error: String?,
    val tracks: List<TrackDomain>,
)

val topSongsDummy = TopSongsDomain(
    success = true,
    error = null,
    tracks = trackDummy,
)