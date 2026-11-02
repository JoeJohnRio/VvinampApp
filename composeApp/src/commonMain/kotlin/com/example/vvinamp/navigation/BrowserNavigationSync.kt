package com.example.vvinamp.navigation

import androidx.compose.runtime.Composable

@Composable
expect fun BindBrowserNavigation(
    navigator: Navigator,
    navigationState: NavigationState
)
