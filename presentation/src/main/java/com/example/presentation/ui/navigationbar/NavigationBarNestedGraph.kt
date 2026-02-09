package com.example.presentation.ui.navigationbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.presentation.navigation.NavigationState
import com.example.presentation.navigation.Navigator
import com.example.presentation.navigation.graph.BottomNavDestination
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.home.HomeScreen
import com.example.presentation.ui.home.HomeViewModel
import com.example.presentation.navigation.toEntries
import com.example.presentation.ui.home.artist.ArtistScreen
import com.example.presentation.ui.settings.SettingsScreen

@Composable
fun MainNavDisplay(
    navigationState: NavigationState,
    navigator: Navigator,
    bottomNavRouter: BottomNavRouter
) {
    val entryProvider = entryProvider {
        MainNavGraph(
            bottomNavRouter = bottomNavRouter
        )
    }
    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = {
            navigator.goBack()
        },
    )
}

@Composable
fun EntryProviderScope<NavKey>.MainNavGraph(
    bottomNavRouter: BottomNavRouter
) {
    val viewModel: HomeViewModel = hiltViewModel()
    entry<BottomNavDestination.Home> {
        HomeScreen(
            bottomNavRouter = bottomNavRouter,
            viewModel = viewModel,
        )
    }

    entry<BottomNavDestination.Search> {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {

        }
        Text(
            text = "Search",
            fontSize = 100.sp
        )
    }

    entry<BottomNavDestination.Library> {
        Text(
            text = "Library",
            fontSize = 100.sp
        )
    }

    entry<BottomNavDestination.Premium> {
        Text(
            text = "Premium",
            fontSize = 100.sp
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
            history = key.history,
            onBackClick = bottomNavRouter::navigateBack
        )
    }
}