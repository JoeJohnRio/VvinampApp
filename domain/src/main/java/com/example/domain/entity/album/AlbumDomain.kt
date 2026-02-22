package com.example.domain.entity.album

import com.example.domain.entity.song.TrackDomain


data class AlbumDomain(
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

val dummyAlbumDomain = AlbumDomain(
    id = "album_thriller_001",
    title = "Thriller",
    artistName = "Michael Jackson",
    releaseDate = "1998-12-31T00:00:00Z",
    coverArt = "https://example.com/images/thriller_cover.jpg",
    artistImage = "https://example.com/images/thriller_cover.jpg",
    trackCount = 9,
    totalDuration = 2536, // 42 minutes and 16 seconds converted to seconds
    tracks = dummyListOfSimplifiedTracks
)

