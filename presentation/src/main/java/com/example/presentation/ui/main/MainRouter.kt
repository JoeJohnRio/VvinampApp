package com.example.presentation.ui.main

import androidx.navigation.NavHostController
import com.example.presentation.navigation.Page

class MainRouter(
    private val mainNavController: NavHostController
) {

    fun navigateToSearch() {
        mainNavController.navigate(Page.Search)
    }

    fun navigateToAuthMethodSelectionPage() {
        mainNavController.navigate(Page.RegisterMethodSelection)
    }

    fun navigateToRegisterEmailPage() {
        mainNavController.navigate(Page.RegisterEmail)
    }

    fun navigateToRegisterPasswordPage() {
        mainNavController.navigate(Page.RegisterPassword)
    }

    fun navigateToRegisterBirthDatePage() {
        mainNavController.navigate(Page.RegisterBirthDate)
    }

    fun navigateToRegisterGenderPage() {
        mainNavController.navigate(Page.RegisterGender)
    }

    fun navigateToRegisterNamePage() {
        mainNavController.navigate(Page.RegisterName)
    }

    fun navigateToLoginEmailPage() {
        mainNavController.navigate(Page.LoginEmail)
    }

    fun navigateToLoginMethodSelectionPage() {
        mainNavController.navigate(Page.LoginMethodSelection)
    }

    fun navigateToMovieDetails(movieId: Int) {
        mainNavController.navigate(Page.MusicDetails(movieId))
    }

    fun navigateToNavigationBar() {
        mainNavController.navigate(Page.NavigationBar)
    }

    fun navigateBack() {
        mainNavController.popBackStack()
    }
}