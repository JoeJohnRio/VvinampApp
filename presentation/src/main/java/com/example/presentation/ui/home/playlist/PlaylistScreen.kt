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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {


            PlaylistHeroImage(
                innerPadding = innerPadding,
                albumDomain = uiState.album,
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
                        0f to uiState.dominantColor,
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


            AnimatedTopNavigation(
                innerPadding = innerPadding,
                title = uiState.album?.title.orErrorNoText(),
                topBarAlpha = scrollProgress.topBarProgress,
                topBarNameAlpha = scrollProgress.topBarNameProgress,
                topBarColor = uiState.dominantColor,
                onBackClick = {
                    bottomNavRouter.navigateBack()
                }
            )
        }
    }
}