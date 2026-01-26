package com.example.presentation.ui.widget

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.presentation.navigation.NavigationState
import com.example.presentation.ui.navigationbar.BottomNavigationBarItem

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationBarItem>,
    navigationState: NavigationState,
    onItemClick: (BottomNavigationBarItem) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            val isSelected = item == navigationState.topLevelRoute

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(imageVector = item.imageVector, contentDescription = null)
                },
                label = {
                    Text(text = item.tabName)
                }
            )
        }
    }
}