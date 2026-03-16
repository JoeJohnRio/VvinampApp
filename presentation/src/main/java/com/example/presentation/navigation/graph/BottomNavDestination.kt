package com.example.presentation.navigation.graph

import androidx.navigation3.runtime.NavKey
import com.example.domain.entity.ListeningHistoryDomain
import kotlinx.serialization.Serializable

sealed interface BottomNavDestination: NavKey {
    @Serializable
    data object Home : MainDestination

    @Serializable
    data object Search : MainDestination

    @Serializable
    data object Library : MainDestination
    @Serializable
    data object CreatePlaylist : MainDestination
    @Serializable
    data class PlaylistDetail(val id: String): MainDestination
    @Serializable
    data object SearchResults : MainDestination

    @Serializable
    data object Premium : MainDestination

    @Serializable
    data object Create : MainDestination

    @Serializable
    data class Artist(val artistId: String) : MainDestination
    @Serializable
    data class Album(val albumId: String) : MainDestination
    @Serializable
    data object Settings : MainDestination
}