package com.example.youtubemusicclone.ui.navigationbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.youtubemusicclone.navigation.route
import com.example.youtubemusicclone.ui.main.MainRouter
import com.example.youtubemusicclone.ui.widget.BottomNavigationBar
import com.example.youtubemusicclone.ui.widget.TopBar

@Composable
fun NavigationBarScreen(
    sharedViewModel: NavigationBarSharedViewModel,
    mainRouter: MainRouter,
    darkMode: Boolean,
    onThemeUpdated: () -> Unit,
    nestedNavController: NavHostController,
    content: @Composable () -> Unit
) {
    val uiState = NavigationBarUiState()
    Scaffold(
        topBar = {
            TopBar(
                "",
                darkMode,
                onThemeUpdated = onThemeUpdated,
                onSearchClick = {
                    mainRouter.navigateToSearch()
                }
            )

        },
        bottomBar = {
            BottomNavigationBar(
                items = uiState.bottomItems,
                navController = nestedNavController,
                onItemClick = { bottomItem ->
                    val currentPageRoute = nestedNavController.currentDestination?.route
                    val clickedPageRoute = bottomItem.page
                    val notSamePage = currentPageRoute != clickedPageRoute.route()
                    if (notSamePage) {
                        nestedNavController.navigate(clickedPageRoute) {
                            launchSingleTop = true
                            popUpTo(nestedNavController.graph.findStartDestination().id)
                        }
                    }
                    sharedViewModel.onBottomItemClicked(bottomItem)
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