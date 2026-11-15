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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
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


@Composable
fun PlaylistDetailComponent(
    playlist: PlaylistComplete?,
    paddingTop: PaddingValues,
    onClickShare: () -> Unit = {},
    onClickMore: () -> Unit = {},
    onClickEditDetails: () -> Unit = {}
) {
    val totalPaddingTop = paddingTop.calculateTopPadding() + NormalPadding

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = totalPaddingTop),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(PlaylistNoPhotoSize)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                imageVector = Icons.Default.MusicNote,
                tint = MaterialTheme.colorScheme.surfaceBright,
                contentDescription = null
            )
        }

        Spacer(Modifier.width(16.dp))

        // The title gets the full width; sharing a row with the button squeezed it to a few letters.
        Column(modifier = Modifier.weight(1f)) {
            Text(
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = playlist?.title.orEmpty(),
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 24.sp
                )
            )

            PlaylistCreatorComponent(playlist = playlist)

            Spacer(Modifier.height(8.dp))

            ButtonVariableSizeSolid(
                modifier = Modifier.height(36.dp),
                text = "Edit details",
                textColor = MaterialTheme.colorScheme.onSecondary,
                containerColor = MaterialTheme.colorScheme.secondary,
                onClick = onClickEditDetails
            )
        }
    }

    Spacer(Modifier.height(8.dp))

    Icon(
        imageVector = if (playlist?.isPublic == false) Icons.Default.Lock else Icons.Default.Public,
        contentDescription = if (playlist?.isPublic == false) "Private playlist" else "Public playlist",
        tint = MaterialTheme.colorScheme.surfaceBright
    )

    Spacer(Modifier.height(24.dp))

    Row() {
        Icon(
            modifier = Modifier.clickable {
                onClickShare()
            },
            imageVector = Icons.Default.Share,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceBright
        )


        Spacer(Modifier.width(16.dp))

        Icon(
            modifier = Modifier.clickable {
                onClickMore()
            },
            imageVector = Icons.Filled.MoreVert,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceBright
        )
    }
}

@Preview
@Composable
private fun PlaylistDetailComponentPreview() {
    PreviewTheme {
        PlaylistDetailComponent(
            playlist = dummyPlaylists.first(),
            paddingTop = PaddingValues.Zero
        )
    }
}