package com.example.presentation.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.navigation.Navigator
import com.example.presentation.navigation.graph.BottomNavDestination
import com.example.presentation.navigation.rememberNavigationState
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.navigation.router.MainRouter
import com.example.presentation.ui.navigationbar.MainNavDisplay
import com.example.presentation.ui.navigationbar.MainNavigationBarScreen

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    mainRouter: MainRouter,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is MainUiEvent.NavigateToSettings -> {
                    mainRouter.navigateToSettings()
                }
            }
        }
    }

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

    MainNavigationBarScreen(
        bottomNavRouter = bottomNavRouter,
        navigationState = navigationState,
    ) {
        MainNavDisplay(
            navigationState = navigationState,
            navigator = navigator,
            mainRouter = mainRouter
        )
    }
}