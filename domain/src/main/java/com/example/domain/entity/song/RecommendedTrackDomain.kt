package com.example.domain.entity.song

data class RecommendedTrackDomain(
    val id: String,
    val title: String,
    val duration: Int,
    val streamCount: Int,
    val audioFileUrl: String,
    val albumCoverUrl: String,
    val artistName: String,
    val explicit: Boolean,
    val lyrics: String?,
    val releaseDate: String,
    val isAdded: Boolean = false
)

val dummyRecommendedTracks = listOf(
    RecommendedTrackDomain(
        id = "track_001",
        title = "Neon Dreams",
        duration = 215, // 3:35
        streamCount = 1250400,
        audioFileUrl = "https://cdn.dummy.vvinamp.com/audio/neon_dreams.m3u8",
        albumCoverUrl = "https://cdn.dummy.vvinamp.com/covers/neon_dreams.jpg",
        artistName = "Synthwave Squad",
        explicit = false,
        lyrics = "Driving through the city, under the neon glow...",
        releaseDate = "2023-11-15"
    ),
    RecommendedTrackDomain(
        id = "track_002",
        title = "Street Echoes",
        duration = 184, // 3:04
        streamCount = 8900000,
        audioFileUrl = "https://cdn.dummy.vvinamp.com/audio/street_echoes.m3u8",
        albumCoverUrl = "https://cdn.dummy.vvinamp.com/covers/street_echoes.jpg",
        artistName = "Urban Beats",
        explicit = true,
        lyrics = "Yeah, we taking over the streets tonight...",
        releaseDate = "2024-01-20"
    ),
    RecommendedTrackDomain(
        id = "track_003",
        title = "Morning Coffee",
        duration = 150, // 2:30
        streamCount = 450000,
        audioFileUrl = "https://cdn.dummy.vvinamp.com/audio/morning_coffee.m3u8",
        albumCoverUrl = "https://cdn.dummy.vvinamp.com/covers/morning_coffee.jpg",
        artistName = "Acoustic Folk",
        explicit = false,
        lyrics = null, // Instrumental or lyrics unavailable
        releaseDate = "2022-08-05"
    ),
    RecommendedTrackDomain(
        id = "track_004",
        title = "Heavy Iron",
        duration = 265, // 4:25
        streamCount = 310200,
        audioFileUrl = "https://cdn.dummy.vvinamp.com/audio/heavy_iron.m3u8",
        albumCoverUrl = "https://cdn.dummy.vvinamp.com/covers/heavy_iron.jpg",
        artistName = "Metal Bashers",
        explicit = true,
        lyrics = "Breaking the chains, setting it all on fire!",
        releaseDate = "2023-10-31"
    ),
    RecommendedTrackDomain(
        id = "track_005",
        title = "Midnight Drive",
        duration = 198, // 3:18
        streamCount = 2100000,
        audioFileUrl = "https://cdn.dummy.vvinamp.com/audio/midnight_drive.m3u8",
        albumCoverUrl = "https://cdn.dummy.vvinamp.com/covers/midnight_drive.jpg",
        artistName = "The Cruisers",
        explicit = false,
        lyrics = "Stars above, highway ahead, nothing to lose...",
        releaseDate = "2024-02-14"
    ),
    RecommendedTrackDomain(
        id = "track_006",
        title = "Lo-Fi Study Beats",
        duration = 142, // 2:22
        streamCount = 5600000,
        audioFileUrl = "https://cdn.dummy.vvinamp.com/audio/lofi_study.m3u8",
        albumCoverUrl = "https://cdn.dummy.vvinamp.com/covers/lofi_study.jpg",
        artistName = "Chill Room",
        explicit = false,
        lyrics = null,
        releaseDate = "2021-09-01"
    ),
    RecommendedTrackDomain(
        id = "track_007",
        title = "Summer Pop Anthem",
        duration = 210, // 3:30
        streamCount = 14500000,
        audioFileUrl = "https://cdn.dummy.vvinamp.com/audio/summer_pop.m3u8",
        albumCoverUrl = "https://cdn.dummy.vvinamp.com/covers/summer_pop.jpg",
        artistName = "The Divas",
        explicit = false,
        lyrics = "Dancing in the sun, we're having so much fun...",
        releaseDate = "2023-06-10"
    ),
    RecommendedTrackDomain(
        id = "track_008",
        title = "Rebel Yell",
        duration = 175, // 2:55
        streamCount = 980000,
        audioFileUrl = "https://cdn.dummy.vvinamp.com/audio/rebel_yell.m3u8",
        albumCoverUrl = "https://cdn.dummy.vvinamp.com/covers/rebel_yell.jpg",
        artistName = "Punk Riot",
        explicit = true,
        lyrics = "Stand up, fight back, don't let them take control!",
        releaseDate = "2024-03-01"
    ),
    RecommendedTrackDomain(
        id = "track_009",
        title = "Deep Ocean",
        duration = 320, // 5:20
        streamCount = 120500,
        audioFileUrl = "https://cdn.dummy.vvinamp.com/audio/deep_ocean.m3u8",
        albumCoverUrl = "https://cdn.dummy.vvinamp.com/covers/deep_ocean.jpg",
        artistName = "Ambient Soundscapes",
        explicit = false,
        lyrics = null,
        releaseDate = "2020-12-25"
    ),
    RecommendedTrackDomain(
        id = "track_010",
        title = "Love in Berlin",
        duration = 205, // 3:25
        streamCount = 3400000,
        audioFileUrl = "https://cdn.dummy.vvinamp.com/audio/love_in_berlin.m3u8",
        albumCoverUrl = "https://cdn.dummy.vvinamp.com/covers/love_in_berlin.jpg",
        artistName = "Euro Pop Stars",
        explicit = false,
        lyrics = "Walking down the streets of Kreuzberg with you...",
        releaseDate = "2023-04-18"
    )
)