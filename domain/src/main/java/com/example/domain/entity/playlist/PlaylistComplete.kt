package com.example.domain.entity.playlist

import com.example.domain.entity.user.UserMinimal

data class PlaylistComplete(
    val id: String,
    val title: String,
    val description: String?,
    val creator: UserMinimal,
    val createdAt: String,
    val updatedAt: String?,
    val isPublic: Boolean,
    val coverImage: String?,
    val albumCovers: List<String>,
    val savedCount: Int,
    val totalDuration: Int,
    val isSavedByMe: Boolean
)


val dummyPlaylists = listOf(
    PlaylistComplete(
        id = "pl_001",
        title = "Heavy Deadlift Anthems",
        description = "High-energy tracks for breaking PRs and serious weightlifting sessions.",
        creator = UserMinimal(
            id = "usr_99",
            username = "joel_m",
            profilePicture = "https://dummyimage.com/150/1a1a1a/ffffff&text=JM"
        ),
        createdAt = "2026-04-15T08:30:00Z",
        updatedAt = "2026-05-01T14:20:00Z",
        isPublic = true,
        coverImage = "https://dummyimage.com/600/ff4500/ffffff&text=Deadlift+Anthems",
        albumCovers = listOf(
            "https://dummyimage.com/150/000000/ffffff&text=Album1",
            "https://dummyimage.com/150/333333/ffffff&text=Album2",
            "https://dummyimage.com/150/666666/ffffff&text=Album3"
        ),
        savedCount = 1245,
        totalDuration = 4500, // 75 minutes in seconds
        isSavedByMe = true
    ),
    PlaylistComplete(
        id = "pl_002",
        title = "Late Night Kotlin",
        description = null,
        creator = UserMinimal(
            id = "usr_42",
            username = "joel_m",
            profilePicture = "https://dummyimage.com/150/1a1a1a/ffffff&text=JM"
        ),
        createdAt = "2026-05-01T23:00:00Z",
        updatedAt = null,
        isPublic = false,
        coverImage = null,
        albumCovers = listOf(
            "https://dummyimage.com/150/1e1e1e/4af626&text=LoFi1",
            "https://dummyimage.com/150/2d2d2d/4af626&text=LoFi2"
        ),
        savedCount = 0,
        totalDuration = 7200, // 120 minutes in seconds
        isSavedByMe = false
    ),
    PlaylistComplete(
        id = "pl_003",
        title = "Boss Battle OSTs",
        description = "Epic orchestral and rock tracks from top RPGs and action games.",
        creator = UserMinimal(
            id = "usr_88",
            username = "joel_m",
            profilePicture = "https://dummyimage.com/150/1a1a1a/ffffff&text=JM"
        ),
        createdAt = "2026-02-10T19:15:00Z",
        updatedAt = "2026-04-20T10:05:00Z",
        isPublic = true,
        coverImage = "https://dummyimage.com/600/8b0000/ffffff&text=Boss+Battles",
        albumCovers = listOf(
            "https://dummyimage.com/150/4b0082/ffffff&text=RPG1",
            "https://dummyimage.com/150/00008b/ffffff&text=Action2",
            "https://dummyimage.com/150/2f4f4f/ffffff&text=Strategy3",
            "https://dummyimage.com/150/800000/ffffff&text=RPG4"
        ),
        savedCount = 8902,
        totalDuration = 10800, // 3 hours in seconds
        isSavedByMe = true
    )
)