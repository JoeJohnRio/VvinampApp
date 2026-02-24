package com.example.presentation.ui.main.player

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

// 1. Define the states
enum class PlayerState { EXPANDED, COLLAPSED }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpotifyPlayerSheet(
    // Optional: Pass height of your BottomNavigation if you want the mini player
    // to sit ABOVE it rather than covering it.
    bottomNavHeight: Int = 0
) {
    val density = LocalDensity.current
    val screenHeight = androidx.compose.ui.platform.LocalConfiguration.current.screenHeightDp.dp

    // Height of the mini player bar
    val miniPlayerHeight = 64.dp

    // 2. Setup the AnchoredDraggableState

    val state = remember {
        AnchoredDraggableState(
            initialValue = PlayerState.COLLAPSED,
        )
    }

    // 3. Calculate anchors based on screen size
    // We update anchors when the layout is measured
    val pxScreenHeight = with(density) { screenHeight.toPx() }
    val pxMiniPlayerHeight = with(density) { miniPlayerHeight.toPx() }
//    val pxBottomNavHeight = with(density) { bottomNavHeight.toPx() } // If using standard int, remove toPx()

    SideEffect {
        state.updateAnchors(
            DraggableAnchors {
                PlayerState.EXPANDED at 0f
                // The collapsed position is at the bottom, leaving only the mini player visible
                PlayerState.COLLAPSED at (pxScreenHeight - pxMiniPlayerHeight - bottomNavHeight)
            }
        )
    }

    // 4. Calculate progress (0f = Collapsed, 1f = Expanded)
    // We use this to fade elements in and out
    val offset = if (state.offset.isNaN()) 0f else state.offset
    val collapsedOffset = pxScreenHeight - pxMiniPlayerHeight - bottomNavHeight

    // Progress is 1.0 when Expanded (offset 0), 0.0 when Collapsed
    val expansionProgress = (1f - (offset / collapsedOffset)).coerceIn(0f, 1f)

    // THE CONTAINER
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Important: This offset moves the entire sheet
            .offset { IntOffset(x = 0, y = state.requireOffset().roundToInt()) }
            .anchoredDraggable(state, Orientation.Vertical)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest) // Background of player
    ) {

        // --- CONTENT 1: FULL SCREEN PLAYER ---
        // Visible only when expanding
        FullPlayerContent(
            modifier = Modifier
                .alpha(expansionProgress) // Fade in as we drag up
                .fillMaxSize(),
            onCollapse = {
                // Programmatic collapse (e.g. top-left down arrow)
                // Need a coroutine scope in real app
            }
        )

        // --- CONTENT 2: MINI PLAYER ---
        // Visible only when collapsed
        MiniPlayerContent(
            modifier = Modifier
                .alpha(1f - expansionProgress) // Fade out as we drag up
                .height(miniPlayerHeight)
                .fillMaxWidth()
                .clickable {
                    // Tap to expand
                    // scope.launch { state.animateTo(PlayerState.EXPANDED) }
                },
            height = miniPlayerHeight
        )
    }
}

@Composable
fun MiniPlayerContent(
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tiny Album Art
        Box(modifier = Modifier.size(40.dp).background(Color.Gray, RoundedCornerShape(4.dp)))

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text("Espresso", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text("Sabrina Carpenter", style = MaterialTheme.typography.bodySmall)
        }

        // Mini Play Button
        IconButton(onClick = {}) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
        }
    }
}

@Composable
fun FullPlayerContent(
    modifier: Modifier = Modifier,
    onCollapse: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 48.dp) // Status bar padding
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Drag Handle / Down Arrow
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Collapse",
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Start)
                .clickable { onCollapse() }
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Big Album Art
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.DarkGray, RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Title & Artist
        Text("Espresso", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.Black)
        Text("Sabrina Carpenter", style = MaterialTheme.typography.titleMedium, color = Color.Gray)

        Spacer(modifier = Modifier.weight(1f))

        // Scrubber & Controls would go here
        LinearProgressIndicator(progress = { 0.3f }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(64.dp))
    }
}
