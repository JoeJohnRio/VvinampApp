package com.example.domain.entity.album


data class SimplifiedTrackDomain(
    val id: String,
    val title: String,
    val duration: Int,
    val artistName: String,
    val albumCover: String
)

val dummyListOfSimplifiedTracks = listOf(
    SimplifiedTrackDomain(
        id = "track_01",
        title = "Wanna Be Startin' Somethin'",
        duration = 363, // 6:03
        artistName = "Michael Jackson",
        albumCover = ""
    ),
    SimplifiedTrackDomain(
        id = "track_02",
        title = "Baby Be Mine",
        duration = 260, // 4:20
        artistName = "Michael Jackson",
        albumCover = ""
    ),
    SimplifiedTrackDomain(
        id = "track_03",
        title = "The Girl Is Mine",
        duration = 222, // 3:42
        artistName = "Michael Jackson",
        albumCover = ""
    ),
    SimplifiedTrackDomain(
        id = "track_04",
        title = "Thriller",
        duration = 358, // 5:58
        artistName = "Michael Jackson",
        albumCover = ""
    ),
    SimplifiedTrackDomain(
        id = "track_05",
        title = "Beat It",
        duration = 257, // 4:17
        artistName = "Michael Jackson",
        albumCover = ""
    ),
    SimplifiedTrackDomain(
        id = "track_06",
        title = "Billie Jean",
        duration = 297, // 4:57
        artistName = "Michael Jackson",
        albumCover = ""
    ),
    SimplifiedTrackDomain(
        id = "track_07",
        title = "Human Nature",
        duration = 246, // 4:06
        artistName = "Michael Jackson",
        albumCover = ""
    ),
    SimplifiedTrackDomain(
        id = "track_08",
        title = "P.Y.T. (Pretty Young Thing)",
        duration = 238, // 3:58
        artistName = "Michael Jackson",
        albumCover = ""
    ),
    SimplifiedTrackDomain(
        id = "track_09",
        title = "The Lady in My Life",
        duration = 300, // 5:00
        artistName = "Michael Jackson",
        albumCover = ""
    )
)