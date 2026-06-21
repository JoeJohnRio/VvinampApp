package com.example.presentation.ui.navigationbar.util

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.presentation.ui.main.player.BottomSheetState


@Composable
fun ScrimAlphaBoxAnchored(state: AnchoredDraggableState<BottomSheetState>) {
    // 1. Calculate the current offset, handling the initial NaN state before anchors are ready
    val offset = if (state.offset.isNaN()) 0f else state.offset
    val collapsedAnchor = state.anchors.positionOf(BottomSheetState.COLLAPSED).let {
        if (it.isNaN() || it == 0f) 1f else it
    }

    // 2. Calculate a progress fraction from 0f (fully collapsed) to 1f (fully expanded)
    val expansionProgress = (1f - (offset / collapsedAnchor)).coerceIn(0f, 1f)

    // 3. Multiply progress by your maximum desired alpha (0.5f)
    val scrimAlpha = expansionProgress * 0.5f

    if (scrimAlpha > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = scrimAlpha))
        )
    }
}