package com.example.vvinamp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.data.api.playlist.toTrackDomain
import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.playlistMinimalListDummy
import com.example.domain.entity.song.RecommendedTrackDomain
import com.example.domain.entity.song.dummyRecommendedTracks
import com.example.domain.entity.song.toTrackDomain
import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.ui.home.history.ListeningHistoryCard
import com.example.vvinamp.ui.home.library.widget.image.PlaylistCover
import com.example.vvinamp.ui.main.ScreenWrapper
import com.example.vvinamp.ui.main.playback.PlaybackUiEvent
import com.example.vvinamp.ui.main.playback.PlaybackViewModel
import com.example.vvinamp.ui.theme.TSBodyMediumWhite
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.ui.theme.circularStdFontFamily
import com.example.vvinamp.ui.widget.TopBar
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    bottomNavRouter: BottomNavRouter,
    viewModel: HomeViewModel = koinViewModel(),
    playbackViewModel: PlaybackViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.NavigateToArtistScreen -> {
                    bottomNavRouter.navigateToArtistPage(event.history.contentId)
                }

                is HomeEvent.PlayTrack -> {
                    playbackViewModel.onEvent(
                        PlaybackUiEvent.OnTrackClicked(
                            track = event.history.toTrackDomain(),
                            contentTypeDomain = ContentTypeDomain.TRACK,
                            contentId = event.history.contentId
                        )
                    )
                }

                is HomeEvent.PlayRecommendedTrack -> {
                    playbackViewModel.onEvent(
                        PlaybackUiEvent.OnTrackClicked(
                            track = event.track.toTrackDomain(),
                            contentTypeDomain = ContentTypeDomain.TRACK,
                            contentId = event.track.id
                        )
                    )
                }

                is HomeEvent.NavigateToPlaylistDetail -> {
                    bottomNavRouter.navigateToPlaylistDetail(event.playlistId)
                }

                is HomeEvent.NavigateToPlaylistScreen -> {
                    bottomNavRouter.navigateToPlaylistDetail(event.history.contentId)
                }

                is HomeEvent.NavigateToAlbumScreen -> {
                    bottomNavRouter.navigateToAlbum(event.history.contentId)
                }

                is HomeEvent.NavigateToSettingsScreen -> {
                    bottomNavRouter.navigateToSettings()
                }

                is HomeEvent.NavigateToSearchResultScreen -> {
                    bottomNavRouter.navigateToSearchResults()
                }
            }
        }
    }

    ScreenWrapper(
        isLoading = uiState.isLoading && uiState.recommendedTracks.isEmpty(),
        error = uiState.error,
        onRetry = {
            viewModel.loadHomeData()
        },
        content = {
            HomeContent(
                uiState = uiState,
                onEvent = { viewModel.onEvent(it) }
            )
        }
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState = HomeUiState(),
    onEvent: (HomeUiEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBar(
                "",
                onSearchClick = {
                    onEvent(HomeUiEvent.OnSearchClicked)
                },
                onSettingsClick = {
                    onEvent(HomeUiEvent.OnSettingsClicked)
                }
            )
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding),
            contentPadding = PaddingValues(top = 8.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Recently Played Grid (only if history exists)
            if (uiState.listHistory.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Recently Played",
                            style = TSHeadlineMediumWhite().copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(Modifier.height(10.dp))
                        val recentItems = uiState.listHistory.take(6)
                        recentItems.chunked(2).forEach { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { history ->
                                    ListeningHistoryCard(
                                        history = history,
                                        modifier = Modifier.weight(1f),
                                        onEvent = onEvent
                                    )
                                }
                                if (rowItems.size == 1) {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // 3. Recommended For You Horizontal Carousel
            if (uiState.recommendedTracks.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Recommended for you",
                            style = TSHeadlineMediumWhite().copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(uiState.recommendedTracks.take(8)) { track ->
                                RecommendedTrackCard(
                                    track = track,
                                    onClick = {
                                        onEvent(HomeUiEvent.OnRecommendedTrackClicked(track))
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 4. Featured Playlists Horizontal Carousel
            if (uiState.featuredPlaylists.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Featured Playlists",
                            style = TSHeadlineMediumWhite().copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(uiState.featuredPlaylists) { playlist ->
                                FeaturedPlaylistCard(
                                    playlist = playlist,
                                    onClick = {
                                        onEvent(HomeUiEvent.OnPlaylistClicked(playlist.id))
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 5. YouTube Stream / Discovery Quick Card
            item {
                YouTubeStreamBanner(
                    onSearchClick = {
                        onEvent(HomeUiEvent.OnSearchClicked)
                    }
                )
            }

            // 6. More Songs to Discover (compact track rows)
            if (uiState.recommendedTracks.size > 4) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "More to Explore",
                            style = TSHeadlineMediumWhite().copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        uiState.recommendedTracks.drop(4).take(6).forEach { track ->
                            HomeTrackRow(
                                track = track,
                                onClick = {
                                    onEvent(HomeUiEvent.OnRecommendedTrackClicked(track))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendedTrackCard(
    track: RecommendedTrackDomain,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            AsyncImage(
                model = track.albumCoverUrl,
                contentDescription = track.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = track.title,
            style = TSHeadlineMediumWhite().copy(fontSize = 14.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = track.artistName,
            style = TSBodyMediumWhite().copy(
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.surfaceBright
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FeaturedPlaylistCard(
    playlist: PlaylistMinimal,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick)
    ) {
        PlaylistCover(
            coverImage = playlist.coverImage,
            covers = playlist.albumCovers,
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = playlist.title,
            style = TSHeadlineMediumWhite().copy(fontSize = 14.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = if (playlist.username.isNotEmpty()) "By ${playlist.username}" else "Curated playlist",
            style = TSBodyMediumWhite().copy(
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.surfaceBright
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun YouTubeStreamBanner(
    onSearchClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onSearchClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Stream from YouTube",
                    style = TSHeadlineMediumWhite().copy(fontSize = 15.sp)
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = "Search any song, artist, or paste a link to play instantly",
                    style = TSBodyMediumWhite().copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.surfaceBright
                    )
                )
            }
        }
    }
}

@Composable
private fun HomeTrackRow(
    track: RecommendedTrackDomain,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = track.albumCoverUrl,
            contentDescription = track.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                style = TSHeadlineMediumWhite().copy(fontSize = 14.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artistName,
                style = TSBodyMediumWhite().copy(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.surfaceBright
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = formatDuration(track.duration),
            style = TSBodyMediumWhite().copy(
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            )
        )
        Spacer(Modifier.width(12.dp))
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play",
            tint = MaterialTheme.colorScheme.inversePrimary,
            modifier = Modifier.size(20.dp)
        )
    }
}

private fun formatDuration(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "$m:${if (s < 10) "0$s" else "$s"}"
}
