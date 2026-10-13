package com.example.vvinamp.ui.navigationbar
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.vvinamp.navigation.NavigationState
import com.example.vvinamp.navigation.Navigator
import com.example.vvinamp.navigation.graph.BottomNavDestination
import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.ui.home.HomeScreen
import com.example.vvinamp.navigation.toEntries
import com.example.vvinamp.ui.home.album.AlbumScreen
import com.example.vvinamp.ui.home.artist.ArtistScreen
import com.example.vvinamp.ui.home.library.LibraryScreen
import com.example.vvinamp.ui.home.library.createplaylist.CreatePlaylistScreen
import com.example.vvinamp.ui.home.library.detailplaylist.PlaylistScreen
import com.example.vvinamp.ui.home.library.detailplaylist.editplaylist.PlaylistEditScreen
import com.example.vvinamp.ui.search.SearchScreen
import com.example.vvinamp.ui.search.searchresult.SearchResultsScreen
import com.example.vvinamp.ui.settings.SettingsScreen

import com.example.vvinamp.ui.main.playback.PlaybackViewModel

@Composable
fun MainNavDisplay(
    navigationState: NavigationState,
    navigator: Navigator,
    bottomNavRouter: BottomNavRouter,
    playbackViewModel: PlaybackViewModel
) {
    val entryProvider = entryProvider {
        MainNavGraph(
            bottomNavRouter = bottomNavRouter,
            playbackViewModel = playbackViewModel
        )
    }
    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        }
    )
}

@Composable
fun EntryProviderScope<NavKey>.MainNavGraph(
    bottomNavRouter: BottomNavRouter,
    playbackViewModel: PlaybackViewModel
) {
    entry<BottomNavDestination.Home> {
        HomeScreen(
            bottomNavRouter = bottomNavRouter,
            playbackViewModel = playbackViewModel
        )
    }

    entry<BottomNavDestination.Search> {
        SearchScreen(
            bottomNavRouter = bottomNavRouter
        )
    }

    entry<BottomNavDestination.SearchResults> {
        SearchResultsScreen(
            bottomNavRouter = bottomNavRouter,
            playbackViewModel = playbackViewModel
        )
    }

    entry<BottomNavDestination.Library> {
        LibraryScreen(
            bottomNavRouter = bottomNavRouter
        )
    }

    entry<BottomNavDestination.CreatePlaylist> {
        CreatePlaylistScreen(
            onCancel = {
                bottomNavRouter.navigateBack()
            },
            bottomNavRouter = bottomNavRouter
        )
    }

    entry<BottomNavDestination.PlaylistDetail> {
        PlaylistScreen(
            playlistId = it.id,
            bottomNavRouter = bottomNavRouter,
            playbackViewModel = playbackViewModel
        )
    }

    entry<BottomNavDestination.PlaylistEdit> {
        PlaylistEditScreen(
            playlistId = it.id,
            bottomNavRouter = bottomNavRouter,
            playbackViewModel = playbackViewModel
        )
    }

    entry<BottomNavDestination.Create> {
        Text(
            text = "Create",
            fontSize = 100.sp
        )
    }

    entry<BottomNavDestination.Settings> {
        SettingsScreen()
    }

    entry<BottomNavDestination.Artist> { key ->
        ArtistScreen(
            artistId = key.artistId,
            onBackClick = bottomNavRouter::navigateBack,
            bottomNavRouter = bottomNavRouter,
            playbackViewModel = playbackViewModel
        )
    }

    entry<BottomNavDestination.Album> {
        AlbumScreen(
            albumId = it.albumId,
            bottomNavRouter = bottomNavRouter,
            playbackViewModel = playbackViewModel
        )
    }
}