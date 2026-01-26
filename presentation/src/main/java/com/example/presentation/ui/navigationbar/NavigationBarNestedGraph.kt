package com.example.presentation.ui.navigationbar

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.presentation.navigation.NavigationState
import com.example.presentation.navigation.Navigator
import com.example.presentation.navigation.graph.BottomNavDestination
import com.example.presentation.navigation.graph.MainDestination
import com.example.presentation.navigation.router.MainRouter
import com.example.presentation.ui.home.HomePage
import com.example.presentation.ui.home.HomeViewModel
import com.example.presentation.navigation.toEntries

@Composable
fun MainNavDisplay(
    navigationState: NavigationState,
    navigator: Navigator,
    mainRouter: MainRouter
) {
    val entryProvider = entryProvider {
        BottomNavigationViewGraph(
            mainRouter = mainRouter
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
fun EntryProviderScope<NavKey>.BottomNavigationViewGraph(
    mainRouter: MainRouter
) {
    val viewModel: HomeViewModel = hiltViewModel()
    entry<BottomNavDestination.Home> {
        HomePage(
            mainRouter = mainRouter,
            viewModel = viewModel,
        )
    }

    entry<BottomNavDestination.Search> {
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
}