package com.example.vvinamp.ui.search.searchresult
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.entity.search.dummySearchItems

import com.example.vvinamp.ui.main.playback.PlaybackUiState
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.theme.TSLabelMediumWhite
import com.example.vvinamp.ui.widget.SpotifyPlayingIndicator
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.previewSafePainter
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchItemComponent(
    domain: SearchItemDomain?,
    onClicked: (SearchItemDomain) -> Unit = {},
    playbackUiState: PlaybackUiState = PlaybackUiState(),
    onAddToPlaylistClicked: ((SearchItemDomain.YoutubeTrackSearch) -> Unit)? = null
) {
    when (domain) {
        is SearchItemDomain.TrackSearch -> TrackSearchComponent(domain, onClicked, playbackUiState)

        is SearchItemDomain.AlbumSearch -> AlbumSearchComponent(domain, onClicked)

        is SearchItemDomain.ArtistSearch -> ArtistSearchComponent(domain, onClicked)

        is SearchItemDomain.PlaylistSearch -> Text(
            "Item 3: ${domain}"
        )

        is SearchItemDomain.PodcastEpisodeSearch -> Text(
            "Item 4: ${domain}"
        )

        is SearchItemDomain.PodcastSeriesSearch -> Text(
            "Item 5: ${domain}"
        )

        is SearchItemDomain.YoutubeTrackSearch -> YoutubeSearchComponent(
            domain = domain,
            onClicked = onClicked,
            playbackUiState = playbackUiState,
            onAddToPlaylistClicked = onAddToPlaylistClicked
        )

        null -> Text(
            "Item 6: ${domain}"
        )
    }
}

@Composable
fun YoutubeSearchComponent(
    domain: SearchItemDomain.YoutubeTrackSearch,
    onClicked: (SearchItemDomain.YoutubeTrackSearch) -> Unit,
    playbackUiState: PlaybackUiState = PlaybackUiState(),
    onAddToPlaylistClicked: ((SearchItemDomain.YoutubeTrackSearch) -> Unit)? = null
) {
    val isCurrentTrackPlayed = playbackUiState.currentTrack?.id == domain.id

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClicked(domain) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = domain.thumbnail,
                contentDescription = null,
                placeholder = previewSafePainter(Res.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isCurrentTrackPlayed) {
                        SpotifyPlayingIndicator(
                            isPlaying = playbackUiState.isPlaying,
                            color = MaterialTheme.colorScheme.primary,
                        )

                        Spacer(Modifier.width(4.dp))
                    }

                    Text(
                        text = domain.title,
                        style = TSDisplayMediumWhite().copy(
                            fontSize = 15.sp,
                            color = if (isCurrentTrackPlayed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary,
                        ),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "YouTube • ${domain.channel} • ${domain.durationString}",
                    style = TSLabelMediumWhite().copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline,
                    ),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }

        if (onAddToPlaylistClicked != null) {
            IconButton(
                onClick = { onAddToPlaylistClicked(domain) },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add to playlist",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun TrackSearchComponent(
    domain: SearchItemDomain.TrackSearch,
    onClicked: (SearchItemDomain.TrackSearch) -> Unit,
    playbackUiState: PlaybackUiState = PlaybackUiState()
) {
    val isCurrentTrackPlayed = playbackUiState.currentTrack?.id == domain.id

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClicked(domain) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = domain.albumCoverUrl,
                contentDescription = null,
                 placeholder = previewSafePainter(Res.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Row() {
                    if (isCurrentTrackPlayed) {
                        SpotifyPlayingIndicator(
                            isPlaying = playbackUiState.isPlaying,
                            color = MaterialTheme.colorScheme.primary,
                        )

                        Spacer(Modifier.width(4.dp))
                    }

                    Text(
                        text = domain.title,
                        style = TSDisplayMediumWhite().copy(
                            fontSize = 15.sp,
                            color = if (isCurrentTrackPlayed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary,
                        )
                    )
                }

                Text(
                    text = "Song • ${domain.artistName}",
                    style = TSLabelMediumWhite().copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline,
                    )
                )
            }
        }

        Row(
            modifier = Modifier.weight(0.3f),
        ) {
            IconButton(
                onClick = {
                    //add to liked song
                },
                modifier = Modifier.size(48.dp) // Standard IconButton size
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(
                onClick = {
                    //add to liked song
                },
                modifier = Modifier.size(48.dp) // Standard IconButton size
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun AlbumSearchComponent(
    domain: SearchItemDomain.AlbumSearch,
    onClicked: (SearchItemDomain) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClicked(domain)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = domain.coverArt,
                contentDescription = null,
                placeholder = previewSafePainter(Res.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = domain.title,
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                    )
                )

                Text(
                    text = "Album • ${domain.artistName}",
                    style = TSLabelMediumWhite().copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline,
                    )
                )
            }
        }

        Row() {
            IconButton(
                onClick = {
                    //add to liked song
                },
                modifier = Modifier.size(48.dp) // Standard IconButton size
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun ArtistSearchComponent(
    domain: SearchItemDomain.ArtistSearch,
    onClicked: (SearchItemDomain) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClicked(domain)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = domain.profileImage,
                contentDescription = null,
                placeholder = previewSafePainter(Res.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = domain.name,
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                    )
                )

                Text(
                    text = "Artist",
                    style = TSLabelMediumWhite().copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline,
                    )
                )
            }
        }

        Row() {
            IconButton(
                onClick = {
                    //add to liked song
                },
                modifier = Modifier.size(48.dp) // Standard IconButton size
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(
                onClick = {
                    //add to liked song
                },
                modifier = Modifier.size(48.dp) // Standard IconButton size
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchItemComponentPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            dummySearchItems.forEach {
                SearchItemComponent(it)

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}