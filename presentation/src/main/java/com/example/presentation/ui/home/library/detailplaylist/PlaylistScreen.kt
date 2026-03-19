package com.example.presentation.ui.home.library.detailplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.song.TrackDomain
import com.example.presentation.R
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.home.artist.AnimatedTopNavigation
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.ui.widget.ButtonVariableOutlined
import com.example.presentation.ui.widget.ButtonVariableSizeSolid
import com.example.presentation.util.ExtractDominantColorEffect
import com.example.presentation.util.PaddingUtil
import com.example.presentation.util.PaddingUtil.PlaylistNoPhotoSize
import com.example.presentation.util.orErrorNoText

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
) {
    val scrollState = rememberLazyListState()

    val headerHeightPx = with(LocalDensity.current) { 400.dp.toPx() }
    val topBarStartPx = with(LocalDensity.current) { 255.dp.toPx() }
    val topBarEndPx = with(LocalDensity.current) { 265.dp.toPx() }
    val topBarNameStartPx = with(LocalDensity.current) { 265.dp.toPx() }
    val topBarNameEndPx = with(LocalDensity.current) { 270.dp.toPx() }

    val topBarRange = remember { topBarEndPx - topBarStartPx }
    val topBarNameRange = remember { topBarNameEndPx - topBarNameStartPx }

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
                    Brush.verticalGradient(
                        0f to Color.Blue.copy(alpha = 0.7f),
                        1f to Color.Transparent
                    )
                )
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(Modifier.height(innerPadding.calculateTopPadding() + PaddingUtil.AlbumHeroSize))
                }

                item {
                    PlaylistDetailComponent(uiState.playlist)
                }


                item {
                    Spacer(
                        Modifier
                            .height(1000.dp)
                            .fillMaxWidth()
                    )
                }
            }


            AnimatedTopNavigation(
                innerPadding = innerPadding,
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
fun PlaylistDetailComponent(playlist: PlaylistComplete?) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(PlaylistNoPhotoSize)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(R.drawable.ic_music_note),
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = null
            )
        }

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
                    modifier = Modifier.constrainAs(textRef) {
                        width = Dimension.preferredWrapContent
                        centerVerticallyTo(parent)
                    }.background(Color.Red),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = "My playlist #20",
//                text = playlist?.title.orEmpty(),
                    style = TSHeadlineMediumWhite().copy(
                        fontSize = 32.sp
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
        }
    }
}