package com.example.presentation.ui.home.library.detailplaylist

import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.domain.entity.artist.artistProfileDummy
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.widget.ButtonVariableOutlined
import com.example.presentation.util.ListenerUtils
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun PlaylistActionRow(
    uiState: PlaylistUiState,
    isFollowing: Boolean,
    onFollowClick: () -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //joel456Temporary
        VerticalVideoFeedPreview(imageUrl = uiState.playlist?.creator?.profilePicture .orEmpty())

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

        Spacer(modifier = Modifier.weight(1f)) // Pushes play/shuffle to the right

        IconButton(onClick = onShuffleClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_shuffle), // Use a shuffle drawable
                contentDescription = "Shuffle",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp)) // Pushes play/shuffle to the right

        FloatingActionButton(
            onClick = onPlayClick,
            shape = CircleShape,
            containerColor = Color(0xFF1DB954), // Spotify Green
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
                isFollowing = false,
                onFollowClick = { /* Handle follow click */ },
                onPlayClick = { /* Handle play click */ },
                onShuffleClick = { /* Handle shuffle click */ }
            )
        }
    }
}