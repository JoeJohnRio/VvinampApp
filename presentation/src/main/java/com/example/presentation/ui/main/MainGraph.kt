package com.example.presentation.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.presentation.navigation.Graph
import com.example.presentation.navigation.Page
import com.example.presentation.ui.auth.RegisterMethodSelectionPage
import com.example.presentation.ui.auth.AuthPage
import com.example.presentation.ui.auth.LoginEmailPage
import com.example.presentation.ui.auth.LoginMethodSelectionPage
import com.example.presentation.ui.auth.RegisterBirthDatePage
import com.example.presentation.ui.auth.RegisterEmailPage
import com.example.presentation.ui.auth.RegisterGenderPage
import com.example.presentation.ui.auth.RegisterNamePage
import com.example.presentation.ui.auth.RegisterPasswordPage
import com.example.presentation.ui.navigationbar.NavigationBarNestedGraph
import com.example.presentation.ui.navigationbar.NavigationBarScreen
import com.example.presentation.util.composableHorizontalSlide
import com.example.presentation.util.sharedViewModel

@Composable
fun MainGraph(
    mainNavController: NavHostController,
    darkMode: Boolean,
    onThemeUpdated: () -> Unit
) {
    NavHost(
        navController = mainNavController,
        startDestination = Page.NavigationBar,
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

        composableHorizontalSlide<Page.Register>(
        ) {
            BackHandler {
                mainNavController.popBackStack()
            }
            AuthPage(
                mainRouter = MainRouter(mainNavController),
            )
        }

        composableHorizontalSlide<Page.RegisterMethodSelection> {
            BackHandler {
                mainNavController.popBackStack()
            }
            RegisterMethodSelectionPage(
                mainRouter = MainRouter(mainNavController),
            )
        }

        composableHorizontalSlide<Page.RegisterEmail> {
            BackHandler {
                mainNavController.popBackStack()
            }

            RegisterEmailPage(
                mainRouter = MainRouter(mainNavController),
            )
        }

        composableHorizontalSlide<Page.RegisterPassword> {
            BackHandler {
                mainNavController.popBackStack()
            }
            RegisterPasswordPage(
                mainRouter = MainRouter(mainNavController),
            )
        }

        composableHorizontalSlide<Page.RegisterBirthDate> {
            BackHandler {
                mainNavController.popBackStack()
            }
            RegisterBirthDatePage(
                mainRouter = MainRouter(mainNavController),
            )
        }

        composableHorizontalSlide<Page.RegisterGender> {
            BackHandler {
                mainNavController.popBackStack()
            }
            RegisterGenderPage(
                mainRouter = MainRouter(mainNavController),
            )
        }

        composableHorizontalSlide<Page.RegisterName> {
            BackHandler {
                mainNavController.popBackStack()
            }
            RegisterNamePage(
                mainRouter = MainRouter(mainNavController),
            )
        }

        composableHorizontalSlide<Page.LoginMethodSelection> {
            BackHandler {
                mainNavController.popBackStack()
            }
            LoginMethodSelectionPage(
                mainRouter = MainRouter(mainNavController),
            )
        }

        composableHorizontalSlide<Page.LoginEmail> {
            BackHandler {
                mainNavController.popBackStack()
            }
            LoginEmailPage(
                mainRouter = MainRouter(mainNavController),
            )
        }


    }
}