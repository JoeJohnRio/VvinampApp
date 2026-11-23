package com.example.vvinamp.ui.main.player
import com.example.vvinamp.ui.theme.orThemeBackground
import vvinampapp.composeapp.generated.resources.*

import com.example.vvinamp.util.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Share
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min

import coil3.compose.AsyncImage

import com.example.vvinamp.ui.main.playback.PlaybackUiEvent
import com.example.vvinamp.ui.main.playback.PlaybackUiState
import com.example.vvinamp.util.lockOrientation
import com.example.vvinamp.util.preview.PreviewTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.example.domain.entity.song.trackDummy
import com.example.vvinamp.util.preview.previewSafePainter

enum class BottomSheetState { EXPANDED, COLLAPSED }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerSheet(
    onEvent: (PlaybackUiEvent) -> Unit,
    playbackUiState: PlaybackUiState,
    player: Any?,
    state: AnchoredDraggableState<BottomSheetState>,
    onOpenArtist: (String) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val entranceOffset = remember { Animatable(0f) }
    val entranceAnimated = remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    var wasAtTopWhenGestureStarted by remember { mutableStateOf(true) }

    BackHandler(enabled = state.targetValue == BottomSheetState.EXPANDED) {
        // When the back button is pressed, animate the sheet to collapsed
        scope.launch {
            state.animateTo(BottomSheetState.COLLAPSED)
        }
    }

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (scrollState.isScrollInProgress) {
            wasAtTopWhenGestureStarted = (scrollState.value == 0)
        }
    }
    val nestedScrollConnection = remember(state, scrollState) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val currentOffset = state.requireOffset()

                // Dragging UP (negative delta) to expand the sheet BEFORE scrolling inner list
                return if (delta < 0 && source == NestedScrollSource.Drag && currentOffset > 0f) {
                    val consumed = state.dispatchRawDelta(delta)
                    Offset(x = 0f, y = consumed)
                } else {
                    Offset.Zero
                }
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y

                // Dragging DOWN (positive delta).
                // ONLY collapse if the gesture started when the list was ALREADY at the top.
                return if (delta > 0 && source == NestedScrollSource.Drag && wasAtTopWhenGestureStarted) {
                    val consumedDelta = state.dispatchRawDelta(delta)
                    Offset(x = 0f, y = consumedDelta)
                } else {
                    Offset.Zero
                }
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                val toFling = available.y
                val currentOffset = state.requireOffset()

                // Flinging UP
                return if (toFling < 0 && currentOffset > 0f) {
                    state.animateTo(BottomSheetState.EXPANDED)
                    Velocity(x = 0f, y = toFling)
                } else {
                    Velocity.Zero
                }
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                val toFling = available.y
                val currentOffset = state.requireOffset()

                // If we allowed dragging the sheet down, handle what happens when the user lets go
                if (wasAtTopWhenGestureStarted && currentOffset > 0f) {
                    val collapsedAnchor = state.anchors.positionOf(BottomSheetState.COLLAPSED)
                    val halfWay = if (collapsedAnchor.isNaN()) Float.MAX_VALUE else collapsedAnchor / 2f

                    // If they flung it down hard (velocity > 500) OR dragged past 50% of the screen
                    if (toFling > 500f || currentOffset > halfWay) {
                        state.animateTo(BottomSheetState.COLLAPSED)
                    } else {
                        // Otherwise, snap it back to fully expanded
                        state.animateTo(BottomSheetState.EXPANDED)
                    }
                    return Velocity(x = 0f, y = toFling)
                }

                return Velocity.Zero
            }
        }
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .lockOrientation(isLocked = state.targetValue == BottomSheetState.EXPANDED)
            .onSizeChanged { size ->
                state.updateAnchors(
                    DraggableAnchors {
                        BottomSheetState.EXPANDED at 0f
                        BottomSheetState.COLLAPSED at size.height.toFloat()
                    }
                )
                if (!entranceAnimated.value) {
                    entranceAnimated.value = true
                    scope.launch {
                        entranceOffset.snapTo(size.height.toFloat())
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
                    y = (state.requireOffset() + entranceOffset.value).roundToInt()
                )
            }
            .nestedScroll(nestedScrollConnection)
            .pointerInput(state) {
                // Trackpad and mouse-wheel swipes arrive as scroll events, not drags, so the drag handling
                // never saw them. Pulling down from the top of the player closes it, like a touch swipe.
                var pulled = 0f
                var lastEventMs = 0L
                var startedAtTop = false
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        if (event.type != PointerEventType.Scroll) continue
                        val change = event.changes.first()
                        // A pause longer than this starts a new swipe, so momentum from scrolling back up
                        // through the player doesn't close it on arrival at the top.
                        if (change.uptimeMillis - lastEventMs > 300) {
                            pulled = 0f
                            startedAtTop = scrollState.value == 0
                        }
                        lastEventMs = change.uptimeMillis
                        val dy = change.scrollDelta.y // pixels on web; negative means pulling down
                        if (startedAtTop && dy < 0) {
                            pulled -= dy
                            if (pulled > 150f) {
                                startedAtTop = false
                                scope.launch { state.animateTo(BottomSheetState.COLLAPSED) }
                            }
                        } else if (dy > 0) {
                            pulled = 0f
                        }
                    }
                }
            }
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Vertical
            )
            .clip(RoundedCornerShape(32.dp))
    ) {
        PlayerSheetContent(
            uiState = playbackUiState,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState), // nestedScroll removed from here
            player = player,
            onCollapse = {
                scope.launch { state.animateTo(BottomSheetState.COLLAPSED) }
            },
            onEvent = onEvent,
            maxHeight = maxHeight,
            onOpenArtist = onOpenArtist,
            isExpanded = state.targetValue == BottomSheetState.EXPANDED
        )
    }
}

@Composable
fun PlayerSheetContent(
    modifier: Modifier = Modifier,
    onEvent: (PlaybackUiEvent) -> Unit,
    uiState: PlaybackUiState,
    onCollapse: () -> Unit,
    player: Any?,
    maxHeight: Dp,
    onOpenArtist: (String) -> Unit = {},
    isExpanded: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    0f to uiState.dominantColor.orThemeBackground(),
                    0.5f to MaterialTheme.colorScheme.background
                )
            )
            .padding(top = 48.dp)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PlayerTopBar(
            onEvent = onEvent,
            uiState = uiState,
            onCollapse = onCollapse,
        )

        Spacer(Modifier.weight(1f))

        // Like Spotify: the cover sits under the top bar and the spare space goes above the title.
        BoxWithConstraints(
            modifier = Modifier
                .height(maxHeight * 0.56f)
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            // Square and as big as the space allows, so short windows get a smaller cover instead of
            // one that runs into the title.
            val coverSize = min(this.maxWidth, this.maxHeight)

            val embedId = uiState.youtubeEmbedId
            // The video only exists while the player is open. YouTube's terms don't allow playing it hidden,
            // and a fresh player on reopening gets the right size (one kept off-screen could come back shrunk).
            if (embedId != null && isExpanded) {
                key(uiState.embedSession) {
                    // Square, because 16:9 at phone width falls below YouTube's 200x200 minimum.
                    YoutubeEmbed(
                        videoId = embedId,
                        isPaused = uiState.isEmbedPaused,
                        onPlayingChange = { onEvent(PlaybackUiEvent.OnEmbedPlayingChanged(it)) },
                        onEnded = { onEvent(PlaybackUiEvent.OnEmbedEnded) },
                        onError = { onEvent(PlaybackUiEvent.OnEmbedError(it)) },
                        modifier = Modifier.size(coverSize),
                        startSeconds = uiState.embedPositionSeconds,
                        onPosition = { onEvent(PlaybackUiEvent.OnEmbedPosition(it)) }
                    )
                }
            } else {
                AsyncImage(
                    model = uiState.currentTrack?.albumCoverUrl,
                    contentDescription = null,
                    placeholder = previewSafePainter(Res.drawable.bg_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(coverSize)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(Modifier.weight(1f))

        TrackPlayerComponent(
            uiState = uiState,
            onEvent = onEvent,
            player = player,
            onAddToPlaylistClicked = { com.example.vvinamp.util.showToast("Coming soon") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        ShareTrackComponent()

        Spacer(modifier = Modifier.height(20.dp))

        AboutArtistSolidColoredDesc(
            uiState = uiState,
            onOpenArtist = onOpenArtist
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun ShareTrackComponent() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier
                .size(24.dp),
            onClick = { com.example.vvinamp.util.showToast("Coming soon") }
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                imageVector = Icons.Default.Devices,
                contentDescription = "Multi Platform",
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            IconButton(
                modifier = Modifier
                    .size(24.dp),
                onClick = { com.example.vvinamp.util.showToast("Coming soon") }
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                )
            }

            IconButton(
                modifier = Modifier
                    .size(24.dp),
                onClick = { com.example.vvinamp.util.showToast("Coming soon") }
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "List",
                )
            }
        }
    }
}


@Preview
@Composable
fun PlayerSheetPreview() {
    val playerState = remember {
        AnchoredDraggableState(initialValue = BottomSheetState.EXPANDED)
    }

    PreviewTheme {
        PlayerSheet(
            onEvent = {

            },
            playbackUiState = PlaybackUiState(
                currentTrack = trackDummy.first()
            ),
            player = null,
            state = playerState
        )
    }
}
