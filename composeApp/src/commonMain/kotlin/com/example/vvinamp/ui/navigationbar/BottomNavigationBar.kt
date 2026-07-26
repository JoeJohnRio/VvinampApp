package com.example.vvinamp.ui.navigationbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vvinamp.navigation.NavigationState
import com.example.vvinamp.ui.theme.TSLabelMediumWhite
import com.example.vvinamp.util.verticalGradientBottomNav

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    items: List<BottomNavigationBarItem>,
    navigationState: NavigationState,
    onItemClick: (BottomNavigationBarItem) -> Unit
) {
    val density = LocalDensity.current
    val topInsetPx = with(density) { 16.dp.toPx().toInt() }

    NavigationBar(
        modifier = modifier
            .verticalGradientBottomNav(
                startColor = MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.75f),
                midColor = MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.95f),
                endColor = MaterialTheme.colorScheme.surfaceDim.copy(alpha = 1f)
            )
            .offset(y = (-4).dp) ,
        windowInsets = WindowInsets(      // left, top, right, bottom
                left = 0,
                top = 0,
                right = 0,
                bottom = 8   // dp in px below
            ),
        containerColor = Color.Transparent,
    ) {
        items.forEach { item ->
            val isSelected = item == navigationState.topLevelRoute

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(item) },
                icon = {
                    if (item.imageVector != null) {
                        Icon(imageVector = item.imageVector, contentDescription = null)
                    } else if (item.painter != null) {
                        Icon(painter = item.painter.invoke(), contentDescription = null)
                    }
                },
                label = {
                    Text(
                        text = item.tabName,
                        style = TSLabelMediumWhite().copy(
                            fontSize = 11.sp
                        )
                    )
                }
            )
        }
    }
}