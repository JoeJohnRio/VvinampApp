package com.example.vvinamp.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun BindBrowserNavigation(
    navigator: Navigator,
    navigationState: NavigationState
) {
    // No-op on Android: system BackHandler handles OS back buttons and gestures.
}
