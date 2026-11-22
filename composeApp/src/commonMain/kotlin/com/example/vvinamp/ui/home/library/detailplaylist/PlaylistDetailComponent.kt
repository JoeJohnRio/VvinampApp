package com.example.vvinamp.ui.home.library.detailplaylist
import vvinampapp.composeapp.generated.resources.*

import com.example.vvinamp.util.KmpLogger
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.dummyPlaylists

import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.ui.widget.ButtonVariableSizeSolid
import com.example.vvinamp.util.PaddingSizeUtil
import com.example.vvinamp.util.PaddingSizeUtil.NormalPadding
import com.example.vvinamp.util.PaddingSizeUtil.PlaylistNoPhotoSize
import com.example.vvinamp.util.orErrorNoText
import com.example.vvinamp.util.preview.PreviewTheme


// Header of a playlist with no songs yet: cover, title with "Change", creator.
@Composable
fun PlaylistDetailComponent(
    playlist: PlaylistComplete?,
    isCreator: Boolean = true,
    onClickEditDetails: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(PlaylistNoPhotoSize)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .clickable(enabled = isCreator, onClickLabel = "Edit details", onClick = onClickEditDetails),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(56.dp),
                // Only the owner can edit, so others see a plain music note instead of the pencil.
                imageVector = if (isCreator) Icons.Default.Edit else Icons.Default.MusicNote,
                tint = MaterialTheme.colorScheme.surfaceBright,
                contentDescription = null
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = playlist?.title.orEmpty(),
                    style = TSHeadlineMediumWhite().copy(
                        fontSize = 24.sp
                    )
                )

                if (isCreator) {
                    Spacer(Modifier.width(8.dp))

                    ButtonVariableSizeSolid(
                        modifier = Modifier.height(36.dp),
                        text = "Change",
                        textColor = MaterialTheme.colorScheme.onSecondary,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        onClick = onClickEditDetails
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            PlaylistCreatorRow(playlist)
        }
    }
}

// Rows under the empty header: visibility, share, and the main "Add to this playlist" action.
@Composable
fun PlaylistEmptyActions(
    playlist: PlaylistComplete?,
    isCreator: Boolean = true,
    onClickShare: () -> Unit = {},
    onClickAdd: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(8.dp))

        Icon(
            imageVector = if (playlist?.isPublic == false) Icons.Default.Lock else Icons.Default.Public,
            contentDescription = if (playlist?.isPublic == false) "Private playlist" else "Public playlist",
            tint = MaterialTheme.colorScheme.surfaceBright
        )

        Spacer(Modifier.height(8.dp))

        IconButton(
            modifier = Modifier.offset(x = (-12).dp),
            onClick = onClickShare
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = MaterialTheme.colorScheme.surfaceBright
            )
        }

        if (!isCreator) return@Column

        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(48.dp),
            onClick = onClickAdd,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Add to this playlist",
                style = TSHeadlineMediumWhite().copy(fontSize = 16.sp, color = Color.Black)
            )
        }
    }
}

@Preview
@Composable
private fun PlaylistDetailComponentPreview() {
    PreviewTheme {
        PlaylistDetailComponent(
            playlist = dummyPlaylists.first()
        )
    }
}