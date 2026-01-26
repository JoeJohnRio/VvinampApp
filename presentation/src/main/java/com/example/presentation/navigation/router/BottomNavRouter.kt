package com.example.presentation.navigation.router

import com.example.presentation.navigation.Navigator
import com.example.presentation.navigation.graph.BottomNavDestination

class BottomNavRouter(
    private val navigator: Navigator
) {
    fun navigateToHome() {
        navigator.navigate(BottomNavDestination.Home)
    }

    fun navigateToSearch() {
        navigator.navigate(BottomNavDestination.Search)
    }

    fun navigateToLibrary() {
        navigator.navigate(BottomNavDestination.Library)
    }

    fun navigateToPremium() {
        navigator.navigate(BottomNavDestination.Premium)
    }

    fun navigateToCreate() {
        navigator.navigate(BottomNavDestination.Create)
    }
}