package com.example.domain.entity.song

import com.example.domain.entity.ListeningHistoryDomain
import kotlinx.serialization.Serializable

@Serializable
data class TrackDomain(
    val id: String,
    val artistName: String,
    val albumCoverUrl: String,
    val title: String,
    val duration: Int,
    val streamCount: Int,
    val explicit: Boolean,
    val releaseDate: String,
)
//joel456 Temporary
fun ListeningHistoryDomain.toTrackDomain(): TrackDomain {
    return TrackDomain(
        id = contentId,
        artistName = contentSubtitle,
        albumCoverUrl = contentImage.firstOrNull().orEmpty(),
        title = contentName,
        duration = 0,
        streamCount = 0,
        explicit = false,
        releaseDate = ""
    )
}
val trackDummy =  listOf(
    TrackDomain(
        id = "track_001",
        artistName = "Michael Jackson",
        albumCoverUrl = "https://example.com/covers/thriller.jpg",
        title = "Billie Jean",
        duration = 294, // 4:54 in seconds
        streamCount = 1500000000,
        explicit = false,
        releaseDate = "1982-11-29"
    ),
    TrackDomain(
        id = "track_002",
        artistName = "Michael Jackson",
        albumCoverUrl = "https://example.com/covers/thriller.jpg",
        title = "Beat It",
        duration = 258, // 4:18 in seconds
        streamCount = 1200000000,
        explicit = false,
        releaseDate = "1982-11-29"
    ),
    TrackDomain(
        id = "track_003",
        artistName = "Michael Jackson",
        albumCoverUrl = "https://example.com/covers/bad.jpg",
        title = "Smooth Criminal",
        duration = 257, // 4:17 in seconds
        streamCount = 1100000000,
        explicit = false,
        releaseDate = "1987-08-31"
    ),
    TrackDomain(
        id = "track_004",
        artistName = "Michael Jackson",
        albumCoverUrl = "https://example.com/covers/thriller.jpg",
        title = "Thriller",
        duration = 357, // 5:57 in seconds
        streamCount = 950000000,
        explicit = false,
        releaseDate = "1982-11-29"
    ),
    TrackDomain(
        id = "track_005",
        artistName = "Michael Jackson",
        albumCoverUrl = "https://example.com/covers/off-the-wall.jpg",
        title = "Rock With You",
        duration = 220, // 3:40 in seconds
        streamCount = 819688869,
        explicit = false,
        releaseDate = "1979-08-10"
    ),
    TrackDomain(
        id = "track_006",
        artistName = "Michael Jackson",
        albumCoverUrl = "https://example.com/covers/off-the-wall.jpg",
        title = "Don't Stop 'Til You Get Enough",
        duration = 363, // 6:03 in seconds
        streamCount = 685399970,
        explicit = false,
        releaseDate = "1979-08-10"
    ),
    TrackDomain(
        id = "track_007",
        artistName = "Michael Jackson",
        albumCoverUrl = "https://example.com/covers/bad.jpg",
        title = "The Way You Make Me Feel",
        duration = 298, // 4:58 in seconds
        streamCount = 677622132,
        explicit = false,
        releaseDate = "1987-08-31"
    ),
    TrackDomain(
        id = "track_008",
        artistName = "Michael Jackson",
        albumCoverUrl = "https://example.com/covers/bad.jpg",
        title = "Man in the Mirror",
        duration = 319, // 5:19 in seconds
        streamCount = 620000000,
        explicit = false,
        releaseDate = "1987-08-31"
    ),
    TrackDomain(
        id = "track_009",
        artistName = "Michael Jackson",
        albumCoverUrl = "https://example.com/covers/bad.jpg",
        title = "Bad",
        duration = 247, // 4:07 in seconds
        streamCount = 580000000,
        explicit = false,
        releaseDate = "1987-08-31"
    ),
    TrackDomain(
        id = "track_010",
        artistName = "Michael Jackson",
        albumCoverUrl = "https://example.com/covers/dangerous.jpg",
        title = "Black or White",
        duration = 251, // 4:11 in seconds
        streamCount = 550000000,
        explicit = false,
        releaseDate = "1991-11-26"
    )
)

