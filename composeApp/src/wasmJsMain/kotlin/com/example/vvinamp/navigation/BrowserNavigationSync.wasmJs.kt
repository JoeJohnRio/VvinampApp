package com.example.vvinamp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.browser.window
import org.w3c.dom.events.Event

@OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
@JsFun(
    """(url) => {
        window.history.pushState(null, '', url);
    }"""
)
private external fun pushBrowserHistory(url: String)

@Composable
actual fun BindBrowserNavigation(
    navigator: Navigator,
    navigationState: NavigationState
) {
    var isPoppingFromBrowser by remember { mutableStateOf(false) }
    var previousDepth by remember { mutableIntStateOf(1) }

    // 1. Listen for browser back / forward / swipe-back
    DisposableEffect(navigator, navigationState) {
        val handlePopState = { _: Event ->
            val currentStack = navigationState.backStacks[navigationState.topLevelRoute]
            val canGoBack = (currentStack != null && currentStack.size > 1) ||
                    (navigationState.topLevelRoute != navigationState.startRoute)

            if (canGoBack) {
                isPoppingFromBrowser = true
                navigator.goBack()
                isPoppingFromBrowser = false
            }
        }

        window.addEventListener("popstate", handlePopState)
        onDispose {
            window.removeEventListener("popstate", handlePopState)
        }
    }

    // 2. Track stack changes to push browser history entries
    val currentStack = navigationState.backStacks[navigationState.topLevelRoute]
    val currentRoute = currentStack?.lastOrNull() ?: navigationState.topLevelRoute
    val currentDepth = (currentStack?.size ?: 1) + if (navigationState.topLevelRoute != navigationState.startRoute) 1 else 0

    LaunchedEffect(currentRoute, currentDepth) {
        if (!isPoppingFromBrowser) {
            if (currentDepth > previousDepth) {
                val routeName = currentRoute::class.simpleName ?: "view"
                pushBrowserHistory("#$routeName")
            }
        }
        previousDepth = currentDepth
    }
}
