package com.example.presentation.ui.navigationbar

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.presentation.navigation.Page
import com.example.presentation.ui.home.HomePage
import com.example.presentation.ui.home.HomeViewModel
import com.example.presentation.ui.main.MainRouter
import com.example.presentation.util.composableHorizontalSlide
import com.example.presentation.util.sharedViewModel
import kotlin.reflect.KClass

@Composable
fun NavigationBarNestedGraph(
    navController: NavHostController,
    mainNavController: NavHostController,
    parentRoute: KClass<*>?
) {
    NavHost(
        navController = navController,
        startDestination = Page.Music,
        route = parentRoute
    ) {
        composableHorizontalSlide<Page.Music> { backStack ->
            Text(
                text = "Feed",
                fontSize = 100.sp
            )

            val viewModel = hiltViewModel<HomeViewModel>()
            HomePage(
                mainRouter = MainRouter(mainNavController),
                viewModel = viewModel,
                sharedViewModel = backStack.sharedViewModel(navController = mainNavController)
            )
        }
        composableHorizontalSlide<Page.Samples> {
            Text(
                text = "Favorites",
                fontSize = 100.sp
            )
//            val viewModel = hiltViewModel<FavoritesViewModel>()
//            FavoritesPage(
//                mainRouter = MainRouter(mainNavController),
//                viewModel = viewModel,
//            )
        }


        composableHorizontalSlide<Page.Explore> {
            Text(
                text = "Explore",
                fontSize = 100.sp
            )
        }

        composableHorizontalSlide<Page.Library> {
            Text(
                text = "Library",
                fontSize = 100.sp
            )
        }
    }
}