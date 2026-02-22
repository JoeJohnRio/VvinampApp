package com.example.domain.entity.song

import kotlinx.serialization.Serializable

@Serializable
data class SimplifiedAlbumDomain(
    val id: String,
    val title: String,
    val releaseDate: String,
    val coverArt: String?,
)


val dummySimplifiedAlbumDomain = listOf(
    SimplifiedAlbumDomain(
        id = "1",
        title = "Got to Be There",
        releaseDate = "1972-01-24",
        coverArt = "https://example.com/got-to-be-there.jpg"
    ),
    SimplifiedAlbumDomain(
        id = "2",
        title = "Ben",
        releaseDate = "1972-08-08",
        coverArt = "https://example.com/ben.jpg"
    ),
    SimplifiedAlbumDomain(
        id = "3",
        title = "Music & Me",
        releaseDate = "1973-09-12",
        coverArt = "https://example.com/music-and-me.jpg"
    ),
    SimplifiedAlbumDomain(
        id = "4",
        title = "Forever, Michael",
        releaseDate = "1975-02-17",
        coverArt = "https://example.com/forever-michael.jpg"
    ),
    SimplifiedAlbumDomain(
        id = "5",
        title = "Off the Wall",
        releaseDate = "1979-08-10",
        coverArt = "https://example.com/off-the-wall.jpg"
    ),
    SimplifiedAlbumDomain(
        id = "6",
        title = "Thriller",
        releaseDate = "1982-11-30",
        coverArt = "https://example.com/thriller.jpg"
    ),
    SimplifiedAlbumDomain(
        id = "7",
        title = "Bad",
        releaseDate = "1987-08-31",
        coverArt = "https://example.com/bad.jpg"
    ),
    SimplifiedAlbumDomain(
        id = "8",
        title = "Dangerous",
        releaseDate = "1991-11-26",
        coverArt = "https://example.com/dangerous.jpg"
    ),
    SimplifiedAlbumDomain(
        id = "9",
        title = "HIStory: Past, Present and Future, Book I",
        releaseDate = "1995-06-20",
        coverArt = "https://example.com/history.jpg"
    ),
    SimplifiedAlbumDomain(
        id = "10",
        title = "Invincible",
        releaseDate = "2001-10-30",
        coverArt = "https://example.com/invincible.jpg"
    )
)