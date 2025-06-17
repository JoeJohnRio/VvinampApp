package com.example.presentation.ui.navigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.presentation.navigation.Page

data class NavigationBarUiState(
    val bottomItems: List<BottomNavigationBarItem> = listOf(
        BottomNavigationBarItem.Feed,
        BottomNavigationBarItem.Samples,
        BottomNavigationBarItem.Explore,
        BottomNavigationBarItem.Library
    )
)

sealed class BottomNavigationBarItem(
    val tabName: String,
    val imageVector: ImageVector,
    val page: Page,
) {
    data object Feed : BottomNavigationBarItem("Feed", imageVector = Icons.Default.Home, Page.Music)
    data object Samples : BottomNavigationBarItem("Samples", imageVector = Icons.Default.PlayArrow, Page.Samples)
    data object Explore : BottomNavigationBarItem("Explore", imageVector = Icons.Default.Explore, Page.Explore)
    data object Library : BottomNavigationBarItem("Library", imageVector = Icons.Default.LibraryMusic, Page.Library)
}
