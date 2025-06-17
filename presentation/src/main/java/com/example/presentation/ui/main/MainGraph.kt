package com.example.presentation.ui.main

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.presentation.navigation.Graph
import com.example.presentation.navigation.Page
import com.example.presentation.ui.auth.AuthPage
import com.example.presentation.ui.navigationbar.NavigationBarNestedGraph
import com.example.presentation.ui.navigationbar.NavigationBarScreen
import com.example.presentation.util.composableHorizontalSlide
import com.example.presentation.util.sharedViewModel
import com.example.presentation.util.toast

@Composable
fun MainGraph(
    mainNavController: NavHostController,
    darkMode: Boolean,
    onThemeUpdated: () -> Unit
) {
    val context = LocalContext.current
    NavHost(
        navController = mainNavController,
        startDestination = Page.Register,
        route = Graph.Main::class
    ) {
        composableHorizontalSlide<Page.NavigationBar> { backStack ->
            val nestedNavController = rememberNavController()
            NavigationBarScreen(
                sharedViewModel = backStack.sharedViewModel(navController = mainNavController),
                mainRouter = MainRouter(mainNavController),
                darkMode = darkMode,
                onThemeUpdated = onThemeUpdated,
                nestedNavController = nestedNavController
            ) {
                NavigationBarNestedGraph(
                    navController = nestedNavController,
                    mainNavController = mainNavController,
                    parentRoute = Graph.Main::class
                )
            }
        }

        composable<Page.Register>(
        ) {
            BackHandler {
                // Do something when back is pressed
                // For example, show a dialog or prevent leaving the screen
                toast(context, "test")
            }
            AuthPage()
        }
    }
}