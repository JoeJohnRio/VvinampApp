package com.example.domain.entity

import com.example.domain.entity.enum.ContentType
import kotlinx.serialization.Serializable

@Serializable
data class ListeningHistoryDomain(
    val id: String,
    val playedAt: String,
    val progress: Int,
    val contentType: ContentType,
    val contentSubtitle: String,
    val contentId: String,
    val contentImage: String,
    val contentName: String
)

val listeningHistoryDummy = listOf(
    // 1. PLAYLIST - 80s Hits Collection
    ListeningHistoryDomain(
        id = "lh_001",
        playedAt = "2026-02-06T22:15:00Z",
        progress = 65,
        contentType = ContentType.PLAYLIST,
        contentId = "playlist_80s_hits_classic",
        contentImage = "https://example.com/80s_playlist_cover.jpg",
        contentName = "Ultimate 80s Classics",
        contentSubtitle = ""
    ),

    // 2. PODCAST_SERIES - The Joe Rogan Experience
    ListeningHistoryDomain(
        id = "lh_002",
        playedAt = "2026-02-06T20:30:00Z",
        progress = 45,
        contentType = ContentType.PODCAST_SERIES,
        contentId = "series_jre_spotify",
        contentImage = "https://example.com/jre_cover.jpg",
        contentName = "The Joe Rogan Experience",
        contentSubtitle = ""
    ),

    // 3. ALBUM - Thriller by Michael Jackson
    ListeningHistoryDomain(
        id = "lh_003",
        playedAt = "2026-02-06T18:45:00Z",
        progress = 85,
        contentType = ContentType.ALBUM,
        contentId = "album_thriller_mj_1982",
        contentImage = "https://example.com/thriller_cover.jpg",
        contentName = "Thriller - Michael Jackson",
        contentSubtitle = ""
    ),

    // 4. ARTIST - Madonna Radio
    ListeningHistoryDomain(
        id = "lh_004",
        playedAt = "2026-02-06T16:20:00Z",
        progress = 50,
        contentType = ContentType.ARTIST,
        contentId = "artist_madonna_radio",
        contentImage = "https://example.com/madonna_profile.jpg",
        contentName = "Madonna Radio",
        contentSubtitle = ""
    ),

    // 5. PLAYLIST - 90s Rock Anthems
    ListeningHistoryDomain(
        id = "lh_005",
        playedAt = "2026-02-06T14:00:00Z",
        progress = 72,
        contentType = ContentType.PLAYLIST,
        contentId = "playlist_90s_rock_anthems",
        contentImage = "https://example.com/90s_rock_playlist.jpg",
        contentName = "90s Rock Anthems",
        contentSubtitle = ""
    ),

    // 6. ALBUM - Purple Rain by Prince
    ListeningHistoryDomain(
        id = "lh_006",
        playedAt = "2026-02-06T12:30:00Z",
        progress = 90,
        contentType = ContentType.ALBUM,
        contentId = "album_purple_rain_prince_1984",
        contentImage = "https://example.com/purple_rain_cover.jpg",
        contentName = "Purple Rain - Prince",
        contentSubtitle = ""
    ),

    // 7. PODCAST_SERIES - Hardcore History
    ListeningHistoryDomain(
        id = "lh_007",
        playedAt = "2026-02-06T10:15:00Z",
        progress = 30,
        contentType = ContentType.PODCAST_SERIES,
        contentId = "series_hardcore_history_dancarlin",
        contentImage = "https://example.com/hardcore_history_cover.jpg",
        contentName = "Hardcore History - Dan Carlin",
        contentSubtitle = ""
    ),

    // 8. UNKNOWN__ - Corrupted Entry
    ListeningHistoryDomain(
        id = "lh_008",
        playedAt = "2026-02-06T08:00:00Z",
        progress = 0,
        contentType = ContentType.UNKNOWN__,
        contentId = "unknown_content_xyz",
        contentImage = "https://example.com/placeholder.jpg",
        contentName = "Unknown Content",
        contentSubtitle = ""
    )
)

