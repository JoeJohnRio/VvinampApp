package com.example.presentation.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.domain.entity.SessionState
import com.example.presentation.navigation.NavigationState
import com.example.presentation.navigation.Navigator
import com.example.presentation.navigation.graph.AuthDestination
import com.example.presentation.navigation.graph.MainDestination
import com.example.presentation.navigation.rememberNavigationState
import com.example.presentation.navigation.router.AuthRouter
import com.example.presentation.navigation.router.MainRouter
import com.example.presentation.navigation.toEntries
import com.example.presentation.ui.auth.navigation.AuthGraph
import com.example.presentation.ui.widget.LoadingScreen

@Composable
fun MainContent(
    mainViewModel: MainViewModel = hiltViewModel(),
    mainNavController: NavHostController,
    darkMode: Boolean,
    onThemeUpdated: () -> Unit
) {
    val sessionState by mainViewModel.sessionState.collectAsStateWithLifecycle()
    when (sessionState) {
        is SessionState.Loading -> {
            LoadingScreen()
        }

        is SessionState.LoggedIn -> {
            AppNavHost(
                isLoggedIn = true
            )
        }

        is SessionState.LoggedOut -> {
            AppNavHost(
                isLoggedIn = false
            )
        }
    }
}

@Composable
fun AppNavHost(
    isLoggedIn: Boolean,
) {

    val navClassRoot = if (isLoggedIn) {
        MainDestination.Root
    } else {
        AuthDestination.Auth
    }
    val appTopLevel = setOf(navClassRoot)
    val navigationState = rememberNavigationState(
        startRoute = navClassRoot,
        topLevelRoutes = appTopLevel,
    )

    val navigator = remember { Navigator(navigationState) }
    val mainRouter = MainRouter(navigator)
    val entryProvider = entryProvider {
        RootGraph(
            routerRoot = mainRouter,
            navigationState = navigationState,
        )
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = {
            navigator.goBack()
        },
    )
}

fun EntryProviderScope<NavKey>.RootGraph(
    routerRoot: MainRouter,
    navigationState: NavigationState,
) {
    entry<AuthDestination.Auth> {
        val navigationStateAuth = rememberNavigationState(
            startRoute = AuthDestination.AuthRoot,
            topLevelRoutes = setOf(AuthDestination.AuthRoot),
        )

        val authNavigator = remember { Navigator(navigationStateAuth) }

        val authRouter = AuthRouter(authNavigator)

        val entryProviderAuth = entryProvider {
            AuthGraph(authRouter, routerRoot)
        }
        NavDisplay(
            entries = navigationStateAuth.toEntries(entryProviderAuth),
            onBack = {
                authNavigator.goBack()
            },
        )
    }

    entry<MainDestination.Root> {
        val navigationStateMain = rememberNavigationState(
            startRoute = MainDestination.NavigationBar,
            topLevelRoutes = setOf(MainDestination.NavigationBar),
        )

        val navigatorMain = remember { Navigator(navigationStateMain) }

        val entryProviderAuth = entryProvider {
            MainGraph()
        }
        NavDisplay(
            entries = navigationStateMain.toEntries(entryProviderAuth),
            onBack = {
                navigatorMain.goBack()
            },
        )
    }
}

