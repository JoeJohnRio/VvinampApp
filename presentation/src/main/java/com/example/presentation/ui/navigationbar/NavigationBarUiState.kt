package com.example.presentation.ui.navigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.presentation.navigation.graph.BottomNavDestination
import com.example.presentation.navigation.graph.MainDestination

data class NavigationBarUiState(
    val bottomItems: List<BottomNavigationBarItem> = listOf(
        BottomNavigationBarItem.Home,
        BottomNavigationBarItem.Search,
        BottomNavigationBarItem.Library,
        BottomNavigationBarItem.Premium,
        BottomNavigationBarItem.Create
    )
)

sealed class BottomNavigationBarItem(
    val tabName: String,
    val imageVector: ImageVector,
    val page: MainDestination,
) {
    data object Home : BottomNavigationBarItem("Home", imageVector = Icons.Default.Home, BottomNavDestination.Home)
    data object Search : BottomNavigationBarItem("Search", imageVector = Icons.Default.PlayArrow, BottomNavDestination.Search)
    data object Library : BottomNavigationBarItem("Library", imageVector = Icons.Default.Explore, BottomNavDestination.Library)
    data object Premium : BottomNavigationBarItem("Premium", imageVector = Icons.Default.LibraryMusic, BottomNavDestination.Premium)
    data object Create : BottomNavigationBarItem("Create", imageVector = Icons.Default.LibraryMusic, BottomNavDestination.Create)
}
