package com.example.presentation.navigation.router

import com.example.domain.entity.ListeningHistoryDomain
import com.example.presentation.navigation.Navigator
import com.example.presentation.navigation.graph.BottomNavDestination

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

    fun navigateToPremium() {
        navigator.navigate(BottomNavDestination.Premium)
    }

    fun navigateToCreate() {
        navigator.navigate(BottomNavDestination.Create)
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