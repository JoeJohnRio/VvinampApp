package com.example.vvinamp.navigation.router
import vvinampapp.composeapp.generated.resources.*

import com.example.domain.entity.ListeningHistoryDomain
import com.example.vvinamp.navigation.Navigator
import com.example.vvinamp.navigation.graph.BottomNavDestination

class BottomNavRouter(
    private val navigator: Navigator
) {
    fun navigateToHome() {
        navigator.navigate(BottomNavDestination.Home)
    }

    fun navigateToAlbum(albumId: String) {
        navigator.navigate(BottomNavDestination.Album(albumId))
    }

    fun navigateToSearch() {
        navigator.navigate(BottomNavDestination.Search)
    }

    fun navigateToSearchResults() {
        navigator.navigate(BottomNavDestination.SearchResults)
    }

    fun navigateToLibrary() {
        navigator.navigate(BottomNavDestination.Library)
    }

    fun navigateToCreatePlaylist() {
        navigator.navigate(BottomNavDestination.CreatePlaylist)
    }

    fun navigateToPlaylistDetail(playlistId: String) {
        navigator.navigate(BottomNavDestination.PlaylistDetail(playlistId))
    }

    fun navigateToPlaylistEdit(playlistId: String) {
        navigator.navigate(BottomNavDestination.PlaylistEdit(playlistId))
    }
    fun navigateToArtistPage(
        artistId: String
    ) {
        navigator.navigate(BottomNavDestination.Artist(artistId))
    }

    fun navigateToSettings() {
        navigator.navigate(BottomNavDestination.Settings)
    }

    fun navigateBack() {
        navigator.goBack()
    }

}