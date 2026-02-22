package com.example.presentation.ui.home.album

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.data.util.dateToYear
import com.example.domain.entity.album.AlbumDomain
import com.example.domain.entity.album.dummyAlbumDomain
import com.example.presentation.R
import com.example.presentation.ui.home.artist.VerticalVideoFeedPreview
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.util.orErrorNoText
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun AlbumDetailComponent(
    data: AlbumDomain?,
    onShuffleClick: () -> Unit = {},
    onPlayClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = data?.artistImage,
                contentDescription = null,
                placeholder = painterResource(R.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = data?.artistName.orErrorNoText(),
                style = TSDisplayMediumWhite().copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Album ∙ ${data?.releaseDate?.dateToYear()}",
            style = TSDisplayMediumWhite().copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticalVideoFeedPreview(imageUrl = data?.artistImage.orEmpty())

            Spacer(modifier = Modifier.width(24.dp))

            IconButton(onClick = {

            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_plus_circle),
                    contentDescription = "More",
                    tint = MaterialTheme.colorScheme.outline,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            IconButton(onClick = { /* Open Menu */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_download_circle),
                    contentDescription = "More",
                    tint = MaterialTheme.colorScheme.outline,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            IconButton(onClick = { /* Open Menu */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_bullet_horizontal),
                    contentDescription = "More",
                    tint = MaterialTheme.colorScheme.outline,
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
}

@Preview
@Composable
fun AlbumDetailComponentPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AlbumDetailComponent(
                data = dummyAlbumDomain,
            )
        }
    }
}
