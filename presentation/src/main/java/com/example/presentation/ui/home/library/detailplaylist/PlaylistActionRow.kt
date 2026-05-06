package com.example.presentation.ui.home.library.detailplaylist

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.presentation.R
import com.example.presentation.ui.main.playback.PlaybackUiState
import com.example.presentation.ui.widget.ButtonBlackSmallIcon
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun PlaylistActionRow(
    uiState: PlaylistUiState,
    playbackUiState: PlaybackUiState,
    onEvent: (PlaylistUiEvent) -> Unit = {}
) {
    PlaylistActionTopRow(
        uiState,
        playbackUiState,
        onEvent
    )
    //joel456 when youre user you could edit
    PlaylistActionBottomRow(
        uiState,
        onEvent
    )
}

@Composable
fun PlaylistActionTopRow(
    uiState: PlaylistUiState,
    playbackUiState: PlaybackUiState,
    onEvent: (PlaylistUiEvent) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //joel456Temporary
        VerticalVideoFeedPreview(imageUrl = uiState.playlist?.creator?.profilePicture.orEmpty())

        Spacer(modifier = Modifier.width(24.dp))

        IconButton(onClick = { /* Open Menu */ }) {
            Icon(
                painter = painterResource(R.drawable.ic_share),
                contentDescription = "More",
                tint = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { /* Open Menu */ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color.Gray
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {
            onEvent(PlaylistUiEvent.ToggleShuffleClicked)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_shuffle),
                contentDescription = "Shuffle",
                tint = if (playbackUiState.isShuffle) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        FloatingActionButton(
            onClick = {
                onEvent(PlaylistUiEvent.onClickPlayAll)
            },
            shape = CircleShape,
            containerColor = Color(0xFF1DB954),
            contentColor = Color.Black,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun VerticalVideoFeedPreview(imageUrl: String) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(32.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.bg_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}

@Composable
fun PlaylistActionBottomRow(
    uiState: PlaylistUiState,
    onEvent: (PlaylistUiEvent) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier.fillMaxWidth()
            .horizontalScroll(scrollState)
    ) {
        ButtonBlackSmallIcon(
            modifier = Modifier
                .padding(top = 12.dp)
                .height(30.dp),
            text = "Add",
            icon = painterResource(R.drawable.ic_create_plus),
        )

        Spacer(Modifier.width(8.dp))

        ButtonBlackSmallIcon(
            modifier = Modifier
                .padding(top = 12.dp)
                .height(30.dp),
            text = "Edit",
            icon = painterResource(R.drawable.ic_paragraph),
        )

        Spacer(Modifier.width(8.dp))

        ButtonBlackSmallIcon(
            modifier = Modifier
                .padding(top = 12.dp)
                .height(30.dp),
            text = "Sort",
            icon = painterResource(R.drawable.ic_arrow_up_down),
        )

        Spacer(Modifier.width(8.dp))

        ButtonBlackSmallIcon(
            modifier = Modifier
                .padding(top = 12.dp)
                .height(30.dp),
            text = "Name & details",
            icon = painterResource(R.drawable.ic_edit),
        )

        Spacer(Modifier.width(8.dp))
    }
}

@Preview
@Composable
fun ArtistActionsRowPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier.fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlaylistActionRow(
                uiState = PlaylistUiState(),
                playbackUiState = PlaybackUiState(
                    isShuffle = true,
                )
            )
        }
    }
}