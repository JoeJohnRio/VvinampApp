package com.example.presentation.ui.main

import androidx.navigation.NavHostController
import com.example.presentation.navigation.Page

class MainRouter(
    private val mainNavController: NavHostController
) {

    fun navigateToSearch() {
        mainNavController.navigate(Page.Search)
    }

    fun navigateToMovieDetails(movieId: Int) {
        mainNavController.navigate(Page.MusicDetails(movieId))
    }
}