package com.example.presentation.navigation.router

import com.example.presentation.navigation.Navigator
//import com.example.domain.entity.User
import com.example.presentation.navigation.graph.AuthDestination

class AuthRouter(
    private val navigator: Navigator
) {
    fun navigateToSearch() {
        navigator.navigate(AuthDestination.Search)
    }

    fun navigateToAuthMethodSelectionPage() {
        navigator.navigate(AuthDestination.RegisterMethodSelection)
    }

    fun navigateToRegisterEmailPage() {
        navigator.navigate(AuthDestination.RegisterEmail)
    }

    fun navigateToRegisterPasswordPage() {
        navigator.navigate(AuthDestination.RegisterPassword)
    }

    fun navigateToRegisterBirthDatePage() {
        navigator.navigate(AuthDestination.RegisterBirthDate)
    }

    fun navigateToRegisterGenderPage() {
        navigator.navigate(AuthDestination.RegisterGender)
    }

    fun navigateToRegisterNamePage() {
        navigator.navigate(AuthDestination.RegisterName)
    }

    fun navigateToLoginEmailPage() {
        navigator.navigate(AuthDestination.LoginEmail)
    }

    fun navigateToLoginMethodSelectionPage() {
        navigator.navigate(AuthDestination.LoginMethodSelection)
    }

    fun navigateToMovieDetails(movieId: Int) {
        navigator.navigate(AuthDestination.MusicDetails(movieId))
    }

    fun popBackToAuthRootPage() {
        navigator.popUntil(AuthDestination.AuthRoot)
    }

    fun navigateBack() {
        navigator.goBack()
    }
}