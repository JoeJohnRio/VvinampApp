package com.example.presentation.ui.main.player

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
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
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// 1. Define the states
enum class PlayerState { EXPANDED, COLLAPSED }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpotifyPlayerSheet(
    bottomNavHeight: Int = 0, // in pixels
    state: AnchoredDraggableState<PlayerState>
) {
    // Use a composition-aware scope — no more bare CoroutineScope(Dispatchers.Main)
    val scope = rememberCoroutineScope()

    // Entrance animation: starts at 0f, will be snapped to full height then animated back down to 0f
    val entranceOffset = remember { Animatable(0f) }
    val entranceAnimated = remember { mutableStateOf(false) }

    // Derive expansion progress from actual anchor positions (safer than manual px math)
    val offset = if (state.offset.isNaN()) 0f else state.offset
    val collapsedAnchor = state.anchors.positionOf(PlayerState.COLLAPSED).let {
        if (it.isNaN() || it == 0f) 1f else it // avoid divide-by-zero before anchors are ready
    }
    val expansionProgress = (1f - (offset / collapsedAnchor)).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            // updateAnchors here: fires when size is actually known (layout phase)
            .onSizeChanged { size ->
                state.updateAnchors(
                    DraggableAnchors {
                        PlayerState.EXPANDED at 0f
                        PlayerState.COLLAPSED at (size.height.toFloat() - bottomNavHeight)
                    }
                )
                // Trigger entrance animation exactly once, after we know the real height
                if (!entranceAnimated.value) {
                    entranceAnimated.value = true
                    scope.launch {
                        entranceOffset.snapTo(size.height.toFloat()) // instantly off-screen
                        entranceOffset.animateTo(
                            targetValue = 0f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            )
                        )
                    }
                }
            }
            .offset {
                IntOffset(
                    x = 0,
                    // Drag state offset + entrance animation combined
                    y = (state.requireOffset() + entranceOffset.value).roundToInt()
                )
            }
            .anchoredDraggable(state, Orientation.Vertical)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
    ) {
        FullPlayerContent(
            modifier = Modifier
                .alpha(expansionProgress)
                .fillMaxSize(),
            onCollapse = {
                // Now we have a proper scope here
                scope.launch {
                    state.animateTo(PlayerState.COLLAPSED)
                }
            }
        )
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
        Text(
            "Espresso",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text("Sabrina Carpenter", style = MaterialTheme.typography.titleMedium, color = Color.Gray)

        Spacer(modifier = Modifier.weight(1f))

        // Scrubber & Controls would go here
        LinearProgressIndicator(progress = { 0.3f }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(64.dp))
    }
}
