package com.example.vvinamp.ui.home.artist
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.domain.entity.artist.ArtistProfileDomain
import com.example.domain.entity.artist.artistProfileDummy

import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.widget.ButtonVariableOutlined
import com.example.vvinamp.util.ListenerUtils
import com.example.vvinamp.util.preview.PreviewTheme

@Composable
fun ArtistActionsRow(
    artistProfile: ArtistProfileDomain,
    isFollowing: Boolean,
    onFollowClick: () -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            )
            .padding(
                top = 16.dp,
            )
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = "${ListenerUtils.formatCompact(artistProfile.monthlyListeners.toLong())} monthly listeners",
            style = TSDisplayMediumWhite().copy(
                fontSize = 12.sp
            )
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticalVideoFeedPreview(imageUrl = artistProfile.profileImage.orEmpty())

            Spacer(modifier = Modifier.width(24.dp))

            ButtonVariableOutlined(
                text = if (isFollowing) "Following" else "Follow",
                buttonHeight = 30.dp,
                contentPadding = PaddingValues(horizontal = 12.dp),
                shape = RoundedCornerShape(4.dp),
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontWeight = FontWeight.Bold
                ),
            )

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
                    painter = painterResource(Res.drawable.ic_shuffle), // Use a shuffle drawable
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
            placeholder = painterResource(Res.drawable.bg_image),
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
    val michaelJacksonProfile = artistProfileDummy

    PreviewTheme {
        Column(
            modifier = Modifier.fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ArtistActionsRow(
                artistProfile = michaelJacksonProfile,
                isFollowing = false,
                onFollowClick = { /* Handle follow click */ },
                onPlayClick = { /* Handle play click */ },
                onShuffleClick = { /* Handle shuffle click */ }
            )
        }
    }
}