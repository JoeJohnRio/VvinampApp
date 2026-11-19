package com.example.vvinamp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavKey
import com.example.vvinamp.navigation.graph.BottomNavDestination
import kotlinx.browser.window
import org.w3c.dom.events.Event

@OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
@JsFun(
    """(url) => {
        window.history.pushState(null, '', url);
    }"""
)
private external fun pushBrowserHistory(url: String)

@OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
@JsFun(
    """(url) => {
        window.history.replaceState(null, '', url);
    }"""
)
private external fun replaceBrowserHistory(url: String)

// The URL keeps the item id for screens that need one, so a reload can reopen the same screen.
private fun NavKey.toUrl(): String = when (this) {
    BottomNavDestination.Home -> window.location.pathname
    is BottomNavDestination.PlaylistDetail -> "#playlist/$id"
    is BottomNavDestination.PlaylistEdit -> "#playlist/$id"
    is BottomNavDestination.Album -> "#album/$albumId"
    is BottomNavDestination.Artist -> "#artist/$artistId"
    else -> "#${this::class.simpleName ?: "view"}"
}

// shortcut: only the screens that make sense to land on are restored; anything else opens Home.
private fun routeFromHash(hash: String): NavKey? {
    val parts = hash.removePrefix("#").split("/", limit = 2)
    val id = parts.getOrNull(1)?.takeIf { it.isNotBlank() }
    return when (parts[0]) {
        "playlist" -> id?.let { BottomNavDestination.PlaylistDetail(it) }
        "album" -> id?.let { BottomNavDestination.Album(it) }
        "artist" -> id?.let { BottomNavDestination.Artist(it) }
        "Search" -> BottomNavDestination.Search
        "Library" -> BottomNavDestination.Library
        else -> null
    }
}

@Composable
actual fun BindBrowserNavigation(
    navigator: Navigator,
    navigationState: NavigationState
) {
    var previousDepth by remember { mutableIntStateOf(1) }
    // Read before the effects below rewrite the URL.
    val hashOnLoad = remember { window.location.hash }

    // 1. Listen for browser back / forward / swipe-back
    DisposableEffect(navigator, navigationState) {
        val handlePopState = { _: Event ->
            val currentStack = navigationState.backStacks[navigationState.topLevelRoute]
            val canGoBack = (currentStack != null && currentStack.size > 1) ||
                    (navigationState.topLevelRoute != navigationState.startRoute)

            if (canGoBack) {
                navigator.goBack()
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

    // Going deeper adds a history entry; any other move just updates the URL so it never goes stale.
    LaunchedEffect(currentRoute, currentDepth) {
        if (currentDepth > previousDepth) {
            pushBrowserHistory(currentRoute.toUrl())
        } else {
            replaceBrowserHistory(currentRoute.toUrl())
        }
        previousDepth = currentDepth
    }

    // 3. After a reload, reopen the screen the URL points to.
    LaunchedEffect(Unit) {
        routeFromHash(hashOnLoad)?.let { navigator.navigate(it) }
    }
}
