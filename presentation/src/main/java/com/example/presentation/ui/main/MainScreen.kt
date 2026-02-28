package com.example.presentation.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.navigation.Navigator
import com.example.presentation.navigation.graph.BottomNavDestination
import com.example.presentation.navigation.rememberNavigationState
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.main.playback.PlaybackViewModel
import com.example.presentation.ui.navigationbar.MainNavDisplay
import com.example.presentation.ui.navigationbar.MainScreenContainer

@Composable
fun MainScreen(
    playbackViewModel: PlaybackViewModel = hiltViewModel()
) {
    val appTopLevel =
        setOf(
            BottomNavDestination.Home,
            BottomNavDestination.Search,
            BottomNavDestination.Library,
            BottomNavDestination.Create,
            BottomNavDestination.Premium
        )
    val navigationState = rememberNavigationState(
        startRoute = BottomNavDestination.Home,
        topLevelRoutes = appTopLevel,
    )
    val navigator = remember { Navigator(navigationState) }
    val bottomNavRouter = BottomNavRouter(navigator)

    MainScreenContainer(
        playbackViewModel = playbackViewModel,
        bottomNavRouter = bottomNavRouter,
        navigationState = navigationState,
    ) {
        MainNavDisplay(
            navigationState = navigationState,
            navigator = navigator,
            bottomNavRouter = bottomNavRouter
        )
    }
}