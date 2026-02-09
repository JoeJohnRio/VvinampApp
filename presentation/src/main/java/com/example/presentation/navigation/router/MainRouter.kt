package com.example.presentation.navigation.router

import com.example.domain.entity.ListeningHistoryDomain
import com.example.presentation.navigation.Navigator
import com.example.presentation.navigation.graph.BottomNavDestination
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


    fun navigateToMain() {
        mainNavigator.navigate(MainDestination.Main)
    }

    fun navigateBack() {
        mainNavigator.goBack()
    }
}