package com.example.presentation.ui.navigationbar

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.navigation.NavigationState
import com.example.presentation.navigation.graph.BottomNavDestination
import com.example.presentation.navigation.graph.MainDestination
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.main.MainEvent
import com.example.presentation.ui.main.MainViewModel
import com.example.presentation.ui.widget.BottomNavigationBar
import com.example.presentation.ui.widget.TopBar

@Composable
fun MainNavigationBarScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    bottomNavRouter: BottomNavRouter,
    navigationState: NavigationState,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val uiState = NavigationBarUiState()

    Scaffold(
        topBar = {
            TopBar(
                "",
                onSearchClick = {
                    Toast.makeText(
                        context,
                        "Search clicked",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onSettingsClick = {
                    mainViewModel.onEvent(MainEvent.onSettingsClicked)
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                items = uiState.bottomItems,
                navigationState = navigationState,
                onItemClick = { bottomItem ->
                    when (bottomItem.page) {
                        BottomNavDestination.Home -> bottomNavRouter.navigateToHome()
                        BottomNavDestination.Create -> bottomNavRouter.navigateToCreate()
                        BottomNavDestination.Library -> bottomNavRouter.navigateToLibrary()
                        BottomNavDestination.Premium -> bottomNavRouter.navigateToPremium()
                        BottomNavDestination.Search -> bottomNavRouter.navigateToSearch()
                        else -> {

                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize(1f)
                .padding(paddingValues)
        ) {
            content()
        }
    }
}