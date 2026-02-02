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

val trackDummy = listOf(
    TrackDomain(
        id = "mj_01",
        title = "Billie Jean",
        duration = 294, // 4:54
        streamCount = 1600000000,
        explicit = false,
        releaseDate = "1983-01-02",
        audioFileUrl = "https://example.com/audio/billie_jean.mp3"
    ),
    TrackDomain(
        id = "mj_02",
        title = "Beat It",
        duration = 258, // 4:18
        streamCount = 1100000000,
        explicit = false,
        releaseDate = "1983-02-14",
        audioFileUrl = "https://example.com/audio/beat_it.mp3"
    ),
    TrackDomain(
        id = "mj_03",
        title = "Smooth Criminal",
        duration = 257, // 4:17
        streamCount = 850000000,
        explicit = false,
        releaseDate = "1988-10-21",
        audioFileUrl = "https://example.com/audio/smooth_criminal.mp3"
    ),
    TrackDomain(
        id = "mj_04",
        title = "Thriller",
        duration = 357, // 5:57
        streamCount = 750000000,
        explicit = false,
        releaseDate = "1983-11-30",
        audioFileUrl = "https://example.com/audio/thriller.mp3"
    ),
    TrackDomain(
        id = "mj_05",
        title = "Don't Stop 'Til You Get Enough",
        duration = 365, // 6:05
        streamCount = 900000000,
        explicit = false,
        releaseDate = "1979-07-28",
        audioFileUrl = "https://example.com/audio/dont_stop.mp3"
    ),
    TrackDomain(
        id = "mj_06",
        title = "Man in the Mirror",
        duration = 319, // 5:19
        streamCount = 700000000,
        explicit = false,
        releaseDate = "1988-01-16",
        audioFileUrl = "https://example.com/audio/man_in_the_mirror.mp3"
    )
)