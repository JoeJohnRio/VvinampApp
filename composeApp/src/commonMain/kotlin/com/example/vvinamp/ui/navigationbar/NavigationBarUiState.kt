package com.example.vvinamp.ui.navigationbar
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.res.vectorResource

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
    val painter: @Composable () -> Painter,
    val page: MainDestination,
) {
    data object Home : BottomNavigationBarItem("Home", painter = @Composable{ painterResource(Res.drawable.ic_home) }, BottomNavDestination.Home)
    data object Search : BottomNavigationBarItem("Search", painter = @Composable{ painterResource(Res.drawable.ic_search) }, BottomNavDestination.Search)
    data object Library : BottomNavigationBarItem("Your Library", painter = @Composable{ painterResource(Res.drawable.ic_library) }, BottomNavDestination.Library)
    data object Create : BottomNavigationBarItem("Create", painter = @Composable{ painterResource(Res.drawable.ic_create_plus) }, BottomNavDestination.Create)
}
