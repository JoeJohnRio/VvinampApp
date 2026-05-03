package com.example.domain.entity.playlist

data class PlaylistMinimal(
    val id: String,
    val title: String,
    val albumCovers: List<String>,
    val isSavedByMe: Boolean,
    val savedCount: Int,
    val username: String,
)

val playlistMinimalDummy = PlaylistMinimal(
    id = "pl-001",
    title = "Late Night Coding",
    albumCovers = listOf("https://picsum.photos/seed/coding/400"),
    isSavedByMe = true,
    savedCount = 1240,
    username = "vvinamp_user"
)

val playlistMinimalListDummy = listOf(
    playlistMinimalDummy,
    PlaylistMinimal(
        id = "pl-002",
        title = "Deep Focus Anthems",
        albumCovers = listOf("https://picsum.photos/seed/focus/400"),
        isSavedByMe = false,
        savedCount = 856,
        username = "alex_dev"
    ),
    PlaylistMinimal(
        id = "pl-003",
        title = "Local Favorites",
        albumCovers = listOf(), // Testing placeholder/fallback UI
        isSavedByMe = false,
        savedCount = 12,
        username = "johndoe"
    ),
    PlaylistMinimal(
        id = "pl-004",
        title = "Workout Mix 2026",
        albumCovers = listOf("https://picsum.photos/seed/gym/400"),
        isSavedByMe = true,
        savedCount = 3200,
        username = "powerlifter_99"
    ),
    PlaylistMinimal(
        id = "pl-005",
        title = "A Very Long Playlist Title That Might Need Ellipsis in the UI",
        albumCovers = listOf("https://picsum.photos/seed/long/400"),
        isSavedByMe = false,
        savedCount = 150,
        username = "curator_pro"
    )
)