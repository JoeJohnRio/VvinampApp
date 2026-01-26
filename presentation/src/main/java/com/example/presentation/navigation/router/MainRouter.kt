package com.example.presentation.navigation.router

import com.example.domain.entity.ListeningHistory
import com.example.presentation.navigation.Navigator
import com.example.presentation.navigation.graph.MainDestination

class MainRouter(
    private val mainNavigator: Navigator
) {
    fun navigateToNavigationBar() {
        mainNavigator.navigate(MainDestination.NavigationBar)
    }

    fun navigateToRootNav() {
        mainNavigator.navigate(MainDestination.Root)
    }

    fun navigateToArtistPage(
        history: ListeningHistory
    ) {
        mainNavigator.navigate(MainDestination.Artist(history))
    }

    fun navigateToMain() {
        mainNavigator.navigate(MainDestination.Main)
    }

    fun navigateToSettings() {
        mainNavigator.navigate(MainDestination.Settings)
    }

    fun navigateBack() {
        mainNavigator.goBack()
    }
}