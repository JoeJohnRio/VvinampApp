package com.example.presentation.ui.home.library.detailplaylist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.song.TrackDomain
import com.example.presentation.R
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.home.artist.AnimatedTopNavigation
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.theme.AppColours
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.ui.widget.ButtonVariableSizeSolid
import com.example.presentation.util.ExtractDominantColorEffect
import com.example.presentation.util.PaddingUtil
import com.example.presentation.util.PaddingUtil.NormalPadding
import com.example.presentation.util.PaddingUtil.PlaylistNoPhotoSize
import com.example.presentation.util.orErrorNoText
import com.example.presentation.util.orZero

@Composable
fun PlaylistScreen(
    playlistId: String,
    bottomNavRouter: BottomNavRouter,
    viewModel: PlaylistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playlistTracks = viewModel.playlistTracks.collectAsLazyPagingItems()

    LaunchedEffect(playlistId) {
        viewModel.getPlaylist(playlistId)
    }

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        content = {
            PlaylistContent(
                uiState = uiState,
                playlistTracks = playlistTracks,
                updateDominantColor = { color ->
                    viewModel.updateDominantColor(color)
                },
                onBackClicked = {
                    bottomNavRouter.navigateBack()
                }
            )
        }
    )
}

@Composable
fun PlaylistContent(
    uiState: PlaylistUiState,
    playlistTracks: LazyPagingItems<TrackDomain>,
    updateDominantColor: (Color) -> Unit,
    onBackClicked: () -> Unit,
    onClickShare: () -> Unit = {},
    onClickMore: () -> Unit = {}
) {
    val scrollState = rememberLazyListState()

    //this is the temporary way
    val isSongExist = uiState.playlist?.totalDuration.orZero() > 0

    val headerHeightPx = with(LocalDensity.current) { 400.dp.toPx() }
    val topBarStartPx = with(LocalDensity.current) { 255.dp.toPx() }
    val topBarEndPx = with(LocalDensity.current) { 265.dp.toPx() }
    val topBarNameStartPx = with(LocalDensity.current) { 265.dp.toPx() }
    val topBarNameEndPx = with(LocalDensity.current) { 270.dp.toPx() }

    val topBarRange = remember { topBarEndPx - topBarStartPx }
    val topBarNameRange = remember { topBarNameEndPx - topBarNameStartPx }

    val noSongsBackgroundColor =
        Brush.verticalGradient(
            0f to Color(0xff474747),
            0.35f to MaterialTheme.colorScheme.background
        )

    data class ScrollProgress(
        val topBarProgress: Float,
        val topBarNameProgress: Float,
        val collapseFraction: Float
    )

    val scrollProgress by remember {
        derivedStateOf {
            val isScrolledPastFirst = scrollState.firstVisibleItemIndex > 0

            if (isScrolledPastFirst) {
                ScrollProgress(1f, 1f, 1f)
            } else {
                val currentScroll = scrollState.firstVisibleItemScrollOffset.toFloat()

                ScrollProgress(
                    topBarProgress = ((currentScroll - topBarStartPx) / topBarRange).coerceIn(
                        0f,
                        1f
                    ),
                    topBarNameProgress = ((currentScroll - topBarNameStartPx) / topBarNameRange).coerceIn(
                        0f,
                        1f
                    ),
                    collapseFraction = (currentScroll / headerHeightPx).coerceIn(0f, 1f)
                )
            }
        }
    }

    ExtractDominantColorEffect(
        imageUrl = uiState.playlist?.coverImage,
        onColorExtracted = { color ->
            updateDominantColor(color)
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    noSongsBackgroundColor
                )
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (isSongExist) {
                    item {
                        Text("Nahhhh")
                    }
                } else {
                    item {
                        Spacer(Modifier.height(innerPadding.calculateTopPadding()))
                    }

                    item {
                        PlaylistDetailComponent(
                            playlist = uiState.playlist,
                            paddingTop = innerPadding
                        )
                    }

                    item {
                        Spacer(Modifier.height(32.dp))
                    }

                    item {
                        RecommendedSongsComponent()
                    }

                    item {
                        Spacer(
                            Modifier
                                .height(1000.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }


            AnimatedTopNavigation(
                innerPadding = innerPadding,
                drawableId = R.drawable.ic_arrow_back,
                title = uiState.playlist?.title.orErrorNoText(),
                topBarAlpha = scrollProgress.topBarProgress,
                topBarNameAlpha = scrollProgress.topBarNameProgress,
                topBarColor = uiState.dominantColor,
                onBackClick = {
                    onBackClicked()
                }
            )
        }
    }
}

@Composable
fun PlaylistDetailComponent(
    playlist: PlaylistComplete?,
    paddingTop: PaddingValues,
    onClickShare: () -> Unit = {},
    onClickMore: () -> Unit = {}
) {
    val dummyCreators = listOf(playlist?.creator, playlist?.creator)
    val totalPaddingTop = paddingTop.calculateTopPadding() + NormalPadding

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = totalPaddingTop),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(PlaylistNoPhotoSize)
                .background(
                    color = AppColours.Black175,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(R.drawable.ic_music_note),
                tint = MaterialTheme.colorScheme.surfaceBright,
                contentDescription = null
            )
        }

        Spacer(Modifier.width(16.dp))

        Column() {
            ConstraintLayout(
                modifier = Modifier
            ) {
                val (textRef, btnRef) = createRefs()

                createHorizontalChain(
                    textRef,
                    btnRef,
                    chainStyle = ChainStyle.Packed(0f)
                )

                Text(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .constrainAs(textRef) {
                            width = Dimension.preferredWrapContent
                            centerVerticallyTo(parent)
                        },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = playlist?.title.orEmpty(),
                    style = TSHeadlineMediumWhite().copy(
                        fontSize = 24.sp
                    )
                )

                ButtonVariableSizeSolid(
                    modifier = Modifier
                        .height(36.dp)
                        .constrainAs(btnRef) {
                            centerVerticallyTo(textRef)
                        },
                    text = "Change",
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .zIndex(1f)
                        .drawBehind {
                            drawCircle(
                                color = Color.Black,
                                blendMode = BlendMode.Clear
                            )
                        }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_create_plus),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                dummyCreators.forEachIndexed { index, dummyCreator ->
                    Log.d("joel123", dummyCreator?.profilePicture.orErrorNoText())
                    Box(
                        modifier = Modifier
                            .offset(x = index.calculateCollaboratorPadding())
                            .zIndex(-index.toFloat()) // Forces the first index to the front
                            .size(24.dp)
                            .drawBehind {
                                drawCircle(
                                    color = Color.Black,
                                    radius = size.width / 2f + 2.dp.toPx(), // +2.dp creates the border gap
                                    blendMode = BlendMode.Clear
                                )
                            }
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                    ) {
                        AsyncImage(
                            model = dummyCreator?.profilePicture,
                            contentDescription = null,
                            placeholder = painterResource(R.drawable.bg_image),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(PaddingUtil.LibraryItemSize)
                        )
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(8.dp))

    Icon(
        painter = painterResource(R.drawable.ic_public),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.surfaceBright
    )

    Spacer(Modifier.height(24.dp))

    Row() {
        Icon(
            modifier = Modifier.clickable {
                onClickShare()
            },
            painter = painterResource(R.drawable.ic_share),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceBright
        )


        Spacer(Modifier.width(16.dp))

        Icon(
            modifier = Modifier.clickable {
                onClickMore()
            },
            painter = painterResource(R.drawable.ic_bullet_vertical),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceBright
        )
    }
}

fun Int.calculateCollaboratorPadding(): Dp {
    val xDistance = (this + 1) * 16
    return xDistance.dp
}