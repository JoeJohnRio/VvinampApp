package com.example.presentation.ui.main.player

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import coil3.compose.AsyncImage
import com.example.domain.entity.song.TrackDomain
import com.example.presentation.R
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.main.playback.PlaybackUiState
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.ui.theme.TSLabelMediumWhite
import com.example.presentation.ui.widget.ButtonVariableOutlined
import com.example.presentation.ui.widget.text.ExpandableText
import com.example.presentation.util.orErrorNoText
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// 1. Define the states
enum class PlayerState { EXPANDED, COLLAPSED }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerSheet(
    uiEvent: (PlaybackUiEvent) -> Unit,
    playbackUiState: PlaybackUiState,
    player: Player?,
    state: AnchoredDraggableState<PlayerState>,
) {
    val scope = rememberCoroutineScope()

    val entranceOffset = remember { Animatable(0f) }
    val entranceAnimated = remember { mutableStateOf(false) }

    val offset = if (state.offset.isNaN()) 0f else state.offset
    val collapsedAnchor = state.anchors.positionOf(PlayerState.COLLAPSED).let {
        if (it.isNaN() || it == 0f) 1f else it // avoid divide-by-zero before anchors are ready
    }
    val expansionProgress = (1f - (offset / collapsedAnchor)).coerceIn(0f, 1f)
    val scrollState = rememberScrollState()
    val isAtTop = scrollState.value == 0

    // 1. Create the nested scroll connection
    val nestedScrollConnection = remember(state) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                // Dragging UP: If sheet isn't fully expanded, expand it first before scrolling
                return if (delta < 0f) {
                    val consumed = state.dispatchRawDelta(delta)
                    Offset(0f, consumed)
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
                // Dragging DOWN: The verticalScroll uses what it needs.
                // If we hit the top, available.y will contain the remaining swipe.
                // We pass that leftover swipe to the draggable state to collapse it.
                return if (delta > 0f) {
                    val dragged = state.dispatchRawDelta(delta)
                    Offset(0f, dragged)
                } else {
                    Offset.Zero
                }
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                val velocity = available.y
                // Flinging UP
                return if (velocity < 0f) {
                    state.animateTo(PlayerState.EXPANDED)
                    Velocity(0f, velocity)
                } else {
                    Velocity.Zero
                }
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                val velocity = available.y
                // Flinging DOWN: Only trigger collapse if the inner scroll didn't consume the fling
                if (velocity > 0f) {
                    state.animateTo(PlayerState.COLLAPSED)
                    return Velocity(0f, velocity)
                }
                return Velocity.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size ->
                state.updateAnchors(
                    DraggableAnchors {
                        PlayerState.EXPANDED at 0f
                        PlayerState.COLLAPSED at size.height.toFloat()
                    }
                )
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
                    y = (state.requireOffset() + entranceOffset.value).roundToInt()
                )
            }
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Vertical,
                enabled = isAtTop
            )
            .clip(RoundedCornerShape(32.dp))
    ) {
        PlayerSheetContent(
            uiState = playbackUiState,
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
                .verticalScroll(scrollState),
            player = player,
            onCollapse = {
                scope.launch {
                    state.animateTo(PlayerState.COLLAPSED)
                }
            },
            uiEvent = uiEvent
        )
    }
}

@Composable
fun PlayerSheetContent(
    modifier: Modifier = Modifier,
    uiEvent: (PlaybackUiEvent) -> Unit,
    uiState: PlaybackUiState,
    onCollapse: () -> Unit,
    player: Player?
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    0f to uiState.dominantColor,
                    0.5f to MaterialTheme.colorScheme.background
                )
            )
            .padding(top = 48.dp)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayerTopBar(
            uiState = uiState,
            onCollapse = onCollapse,
            onMoreOptionClicked = {}
        )

        Column(
            modifier = Modifier.height(550.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = uiState.currentTrack?.albumCoverUrl,
                contentDescription = null,
                placeholder = painterResource(R.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))

            )
        }

        TrackPlayerComponent(
            uiState = uiState,
            uiEvent = uiEvent,
            player = player,
            onAddToPlaylistClicked = {}
        )

        Spacer(modifier = Modifier.height(24.dp))

        ShareTrackComponent()

        Spacer(modifier = Modifier.height(20.dp))

        AboutArtistSolidColoredDesc(
            uiState = uiState
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun AboutArtistSolidColoredDesc(
    uiState: PlaybackUiState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        ImageAboutArtist(uiState.currentTrack?.albumCoverUrl.orEmpty())

        ArtistAboutDetail(uiState.currentTrack)
    }
}

@Composable
fun ArtistAboutDetail(currentTrack: TrackDomain?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = currentTrack?.artistName.orErrorNoText(),
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 18.sp
                    )
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "13.6M monthly listeners",
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.surfaceBright
                    )
                )
            }

            ButtonVariableOutlined(
                modifier = Modifier
                    .height(30.dp)
                    .align(Alignment.TopEnd),
                text = "Follow",
                textStyle = TSDisplayMediumWhite().copy(
                    fontSize = 12.sp
                )
            )
        }

        Spacer(Modifier.height(16.dp))

        ExpandableText(
            text = "Sheila on 7 is an alternative rock band from Yogyakarta, Indonesia. They have changed their lineup several times since their formation on 6 May 1996. Their first lineup consisted of Akhdiyat Duta Modjo (also known as Duta, vocals), Saktia \"Sakti\" Ari Seno (guitar), Eross Candra (guitar), Adam Muhammad Subarkah (bass), and Anton Widiastanto (drums). Anton left the group in 2004, while Sakti left in 2006, leaving their current lineup as it is, but with Brian Kresna Putro replacing Anton in the drums until his departure in 2022.",
            textStyle = TSDisplayMediumWhite().copy(
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.surfaceBright
            )
        )
    }
}

@Composable
fun ImageAboutArtist(
    artistImageUrl: String = ""
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
    ) {

        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = artistImageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.bg_image),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                        0.3f to Color.Transparent
                    )
                )
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp),
            text = stringResource(R.string.title_about_the_artist),
            style = TSHeadlineMediumWhite().copy(
                fontSize = 13.sp
            ),
        )

    }
}

@Composable
fun PlayerTopBar(
    uiState: PlaybackUiState,
    onCollapse: () -> Unit,
    onMoreOptionClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .size(24.dp),
            onClick = onCollapse
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                painter = painterResource(R.drawable.ic_chevron_down),
                contentDescription = "Collapse",
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PLAYING FROM ARTIST",
                style = TSLabelMediumWhite().copy(
                    fontSize = 12.sp
                )
            )

            Text(
                text = uiState.currentTrack?.artistName.orErrorNoText(),
                style = TSDisplayMediumWhite().copy(
                    fontSize = 12.sp
                )
            )
        }

        IconButton(
            modifier = Modifier
                .size(24.dp),
            onClick = onMoreOptionClicked
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                painter = painterResource(R.drawable.ic_bullet_vertical),
                contentDescription = "Detail",
            )
        }
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
            onClick = {}
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                painter = painterResource(R.drawable.ic_multi_platform),
                contentDescription = "Multi Platform",
            )
        }

        Row(

        ) {
            IconButton(
                modifier = Modifier
                    .size(24.dp),
                onClick = {}
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = "Share",
                )
            }

            IconButton(
                modifier = Modifier
                    .size(24.dp),
                onClick = {}
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    painter = painterResource(R.drawable.ic_list),
                    contentDescription = "List",
                )
            }
        }
    }
}