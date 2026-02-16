package com.example.presentation.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.presentation.navigation.graph.MainDestination


@Composable
fun EntryProviderScope<NavKey>.MainGraph() {
    entry<MainDestination.NavigationBar> {
        MainScreen()
    }
}