package com.example.domain.entity.song

import kotlinx.serialization.Serializable


@Serializable
data class TopReleasesDomain(
    val success: Boolean,
    val error: String?,
    val albums: List<SimplifiedAlbumDomain>,
)

val topReleaseDummy = TopReleasesDomain(
    success = true,
    error = null,
    albums = dummySimplifiedAlbumDomain
)
