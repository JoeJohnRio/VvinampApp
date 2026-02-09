package com.example.presentation.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.presentation.navigation.NavigationState
import com.example.presentation.navigation.graph.MainDestination
import com.example.presentation.navigation.router.MainRouter
import com.example.presentation.ui.home.artist.ArtistScreen
import com.example.presentation.ui.settings.SettingsScreen


@Composable
fun EntryProviderScope<NavKey>.MainGraph() {
    entry<MainDestination.NavigationBar> {
        MainScreen()
    }
}