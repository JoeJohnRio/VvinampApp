package com.example.vvinamp.navigation.router

import com.example.vvinamp.navigation.Navigator
import com.example.vvinamp.navigation.graph.BottomNavDestination

interface BottomNavRouter {
    fun navigateToHome()
    fun navigateToAlbum(albumId: String)
    fun navigateToSearch()
    fun navigateToSearchResults()
    fun navigateToLibrary()
    fun navigateToCreatePlaylist()
    fun navigateToPlaylistDetail(playlistId: String)
    fun navigateToPlaylistEdit(playlistId: String)
    fun navigateToArtistPage(artistId: String)
    fun navigateToSettings()
    fun navigateBack()
}

class BottomNavRouterImpl(
    private val navigator: Navigator
) : BottomNavRouter {
    override fun navigateToHome() {
        navigator.navigate(BottomNavDestination.Home)
    }

    override fun navigateToAlbum(albumId: String) {
        navigator.navigate(BottomNavDestination.Album(albumId))
    }

    override fun navigateToSearch() {
        navigator.navigate(BottomNavDestination.Search)
    }

    override fun navigateToSearchResults() {
        navigator.navigate(BottomNavDestination.SearchResults)
    }

    override fun navigateToLibrary() {
        navigator.navigate(BottomNavDestination.Library)
    }

    override fun navigateToCreatePlaylist() {
        navigator.navigate(BottomNavDestination.CreatePlaylist)
    }

    override fun navigateToPlaylistDetail(playlistId: String) {
        navigator.navigate(BottomNavDestination.PlaylistDetail(playlistId))
    }

    override fun navigateToPlaylistEdit(playlistId: String) {
        navigator.navigate(BottomNavDestination.PlaylistEdit(playlistId))
    }

    override fun navigateToArtistPage(artistId: String) {
        navigator.navigate(BottomNavDestination.Artist(artistId))
    }

    override fun navigateToSettings() {
        navigator.navigate(BottomNavDestination.Settings)
    }

    override fun navigateBack() {
        navigator.goBack()
    }
}
