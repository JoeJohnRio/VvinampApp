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
