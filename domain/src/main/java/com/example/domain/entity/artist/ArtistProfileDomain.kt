package com.example.domain.entity.artist

import kotlinx.serialization.Serializable

@Serializable
data class ArtistProfileDomain(
    val id: String,
    val name: String,
    val bio: String?,
    val profileImage: String?,
    val verified: Boolean,
    val monthlyListeners: Int,
)

val artistDummy = ArtistProfileDomain(
    id = "mj_001",
    name = "Michael Jackson",
    bio = "The King of Pop. One of the most significant cultural figures of the 20th century and one of the greatest entertainers in the history of music.",
    profileImage = "https://cirio.my.id/images/artists/sheilaOn7_artist_profile.jpg", // Replace with actual image URL
    verified = true,
    monthlyListeners = 42500000 // approx 42.5M
)
