package com.example.vvinamp.navigation.router

import com.example.domain.entity.ListeningHistoryDomain
import com.example.vvinamp.navigation.Navigator
import com.example.vvinamp.navigation.graph.BottomNavDestination
import com.example.vvinamp.navigation.graph.MainDestination

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