package com.example.vvinamp.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.vvinamp.navigation.graph.MainDestination


@Composable
fun EntryProviderScope<NavKey>.MainGraph() {
    entry<MainDestination.NavigationBar> {
        MainScreen()
    }
}