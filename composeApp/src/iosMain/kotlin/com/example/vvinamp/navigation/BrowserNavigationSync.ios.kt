package com.example.vvinamp.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun BindBrowserNavigation(
    navigator: Navigator,
    navigationState: NavigationState
) {
    // No-op on iOS: native navigation controller and predictive pop handle gestures.
}
