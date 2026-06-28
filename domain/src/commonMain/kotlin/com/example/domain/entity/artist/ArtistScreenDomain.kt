package com.example.domain.entity.artist

import com.example.domain.entity.song.TopReleasesDomain
import com.example.domain.entity.song.TopSongsDomain
import kotlinx.serialization.Serializable

@Serializable
data class ArtistScreenDomain(
    val artistProfile: ArtistProfileDomain?,
    val topSongs: TopSongsDomain?,
    val topReleases: TopReleasesDomain?,
)






