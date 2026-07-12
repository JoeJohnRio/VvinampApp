package com.example.vvinamp.util.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.vvinamp.navigation.Navigator
import com.example.vvinamp.navigation.graph.BottomNavDestination
import com.example.vvinamp.navigation.graph.MainDestination
import com.example.vvinamp.navigation.rememberNavigationState
import com.example.vvinamp.ui.theme.AppTheme
import com.example.vvinamp.ui.theme.appTypography
import com.example.vvinamp.ui.theme.vvinampDarkColorScheme

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
        typography = appTypography(),
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