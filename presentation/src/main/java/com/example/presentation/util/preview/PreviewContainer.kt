package com.example.presentation.util.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.presentation.navigation.Navigator
import com.example.presentation.navigation.graph.BottomNavDestination
import com.example.presentation.navigation.graph.MainDestination
import com.example.presentation.navigation.rememberNavigationState
import com.example.presentation.ui.theme.AppTheme
import com.example.presentation.ui.theme.AppTypography
import com.example.presentation.ui.theme.vvinampDarkColorScheme

@Composable
fun PreviewContainer(
    content: @Composable () -> Unit
) {
    AppTheme {
        content()
    }
}

@Composable
fun PreviewTheme(
    content: @Composable () -> Unit = {}
) {
    // Apply the custom theme here just for the preview
    MaterialTheme(
        colorScheme = vvinampDarkColorScheme,
        typography = AppTypography,
    ) {
        content()
    }
}

@Composable
fun navigatorDummy(): Navigator {
    return Navigator(state = rememberNavigationState(
        startRoute = BottomNavDestination.Home,
        topLevelRoutes = setOf(BottomNavDestination.Home),
    ))
}