package com.example.vvinamp.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.compose.viewmodel.koinViewModel
import com.example.vvinamp.navigation.Navigator
import com.example.vvinamp.navigation.graph.BottomNavDestination
import com.example.vvinamp.navigation.rememberNavigationState
import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.navigation.router.BottomNavRouterImpl
import com.example.vvinamp.ui.main.playback.PlaybackUiEvent
import com.example.vvinamp.ui.main.playback.PlaybackViewModel
import com.example.vvinamp.ui.navigationbar.MainNavDisplay
import com.example.vvinamp.ui.navigationbar.MainScreenContainer

@Composable
fun MainScreen(
    playbackViewModel: PlaybackViewModel = koinViewModel()
) {
    val appTopLevel =
        setOf(
            BottomNavDestination.Home,
            BottomNavDestination.Search,
            BottomNavDestination.Library,
            BottomNavDestination.Create,
        )
    val navigationState = rememberNavigationState(
        startRoute = BottomNavDestination.Home,
        topLevelRoutes = appTopLevel,
    )
    val navigator = remember { Navigator(navigationState) }
    val bottomNavRouter = BottomNavRouterImpl(navigator)

    MainScreenContainer(
        playbackViewModel = playbackViewModel,
        bottomNavRouter = bottomNavRouter,
        navigationState = navigationState,
    ) {
        MainNavDisplay(
            navigationState = navigationState,
            navigator = navigator,
            bottomNavRouter = bottomNavRouter,
            playbackViewModel = playbackViewModel
        )
    }
}