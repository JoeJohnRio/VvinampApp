package com.example.presentation.ui.widget

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.presentation.navigation.NavigationState
import com.example.presentation.ui.navigationbar.BottomNavigationBarItem
import com.example.presentation.ui.theme.TSLabelMediumWhite
import com.example.presentation.util.verticalGradientTopEdge

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationBarItem>,
    navigationState: NavigationState,
    onItemClick: (BottomNavigationBarItem) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .verticalGradientTopEdge(MaterialTheme.colorScheme.background),
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
    ) {
        items.forEach { item ->
            val isSelected = item == navigationState.topLevelRoute

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(imageVector = item.imageVector, contentDescription = null)
                },
                label = {
                    Text(
                        text = item.tabName,
                        style = TSLabelMediumWhite().copy(
                            fontSize = 12.sp
                        )
                    )
                }
            )
        }
    }
}