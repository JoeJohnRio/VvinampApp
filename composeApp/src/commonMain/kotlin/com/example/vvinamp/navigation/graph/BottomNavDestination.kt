package com.example.vvinamp.navigation.graph
import vvinampapp.composeapp.generated.resources.*

import androidx.navigation3.runtime.NavKey
import com.example.domain.entity.ListeningHistoryDomain
import kotlinx.serialization.Serializable

@Serializable
sealed interface BottomNavDestination: NavKey {
    @Serializable
    data object Home : BottomNavDestination

    @Serializable
    data object Search : BottomNavDestination

    @Serializable
    data object Library : BottomNavDestination
    @Serializable
    data object CreatePlaylist : BottomNavDestination
    @Serializable
    data class PlaylistDetail(val id: String): BottomNavDestination
    @Serializable
    data class PlaylistEdit(val id: String): BottomNavDestination
    @Serializable
    data object SearchResults : BottomNavDestination

    @Serializable
    data object Create : BottomNavDestination

    @Serializable
    data class Artist(val artistId: String) : BottomNavDestination
    @Serializable
    data class Album(val albumId: String) : BottomNavDestination
    @Serializable
    data object Settings : BottomNavDestination
}