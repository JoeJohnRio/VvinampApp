package com.example.vvinamp.navigation.router

import com.example.vvinamp.navigation.Navigator
import com.example.vvinamp.navigation.graph.AuthDestination

class AuthRouter(
    private val navigator: Navigator
) {
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

    fun popBackToAuthRootPage() {
        navigator.popUntil(AuthDestination.AuthRoot)
    }

    fun navigateBack() {
        navigator.goBack()
    }
}
