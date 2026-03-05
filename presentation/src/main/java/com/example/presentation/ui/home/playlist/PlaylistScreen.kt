package com.example.presentation.ui.home.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.entity.album.toTrackDomain
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.home.album.AlbumContent
import com.example.presentation.ui.home.album.AlbumDetailComponent
import com.example.presentation.ui.home.album.AlbumHeroImage
import com.example.presentation.ui.home.album.AlbumSongsComponent
import com.example.presentation.ui.home.album.AlbumViewModel
import com.example.presentation.ui.home.artist.AnimatedTopNavigation
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.main.playback.PlaybackViewModel
import com.example.presentation.util.PaddingUtil
import com.example.presentation.util.orErrorNoText

@Composable
fun PlaylistScreen(
    playlistId: String,
    bottomNavRouter: BottomNavRouter,
    viewModel: PlaylistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playbackViewModel: PlaybackViewModel = hiltViewModel()
    val playbackUiState by playbackViewModel.uiState.collectAsStateWithLifecycle()


    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        content = {
            PlaylistContent(
                uiState = uiState
            )
        }
    )
}

@Composable
fun PlaylistContent(
    uiState: PlaylistUiState
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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            PlaylistHeroImage(
                innerPadding = innerPadding,
                albumDomain = null,
                artistNameProgress = scrollProgress.topBarProgress,
                collapseFraction = scrollProgress.collapseFraction,
                modifier = Modifier
                    .height(innerPadding.calculateTopPadding() + PaddingUtil.AlbumHeroSize)
                    .graphicsLayer {
                        val offset = if (scrollState.firstVisibleItemIndex == 0) {
                            -scrollState.firstVisibleItemScrollOffset.toFloat()
                        } else {
                            -headerHeightPx
                        }
                        translationY = offset
                    }
                    .background(Brush.verticalGradient(
                        0f to Color(0xff175faa),
                        1f to MaterialTheme.colorScheme.background
                    )),
            )

            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Spacer(Modifier.height(innerPadding.calculateTopPadding() + PaddingUtil.AlbumHeroSize))
                }

                item {
                }


                item {
                    Spacer(Modifier.height(1000.dp).fillMaxWidth())
                }
            }


//            AnimatedTopNavigation(
//                innerPadding = innerPadding,
//                title = uiState.album?.title.orErrorNoText(),
//                topBarAlpha = scrollProgress.topBarProgress,
//                topBarNameAlpha = scrollProgress.topBarNameProgress,
//                topBarColor = uiState.dominantColor,
//                onBackClick = {
//                    bottomNavRouter.navigateBack()
//                }
//            )
        }
    }
}