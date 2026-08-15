package com.example.vvinamp.ui.navigationbar
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.painterResource

import com.example.vvinamp.navigation.graph.BottomNavDestination
import com.example.vvinamp.navigation.graph.MainDestination

data class NavigationBarUiState(
    val bottomItems: List<BottomNavigationBarItem> = listOf(
        BottomNavigationBarItem.Home,
        BottomNavigationBarItem.Search,
        BottomNavigationBarItem.Library,
        BottomNavigationBarItem.Create
    )
)

sealed class BottomNavigationBarItem(
    val tabName: String,
    val painter: (@Composable () -> Painter)? = null,
    val imageVector: ImageVector? = null,
    val page: BottomNavDestination,
) {
    data object Home : BottomNavigationBarItem(
        tabName = "Home",
        imageVector = Icons.Default.Home,
        page = BottomNavDestination.Home
    )

    data object Search : BottomNavigationBarItem(
        tabName = "Search",
        imageVector = Icons.Default.Search,
        page = BottomNavDestination.Search
    )

    data object Library : BottomNavigationBarItem(
        tabName = "Your Library",
        imageVector = Icons.Default.LibraryMusic,
        page = BottomNavDestination.Library
    )

    data object Create : BottomNavigationBarItem(
        tabName = "Create",
        imageVector = Icons.Default.Add,
        page = BottomNavDestination.Create
    )
}
