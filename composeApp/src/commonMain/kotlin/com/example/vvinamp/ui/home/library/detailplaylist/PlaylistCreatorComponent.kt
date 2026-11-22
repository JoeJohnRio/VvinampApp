package com.example.vvinamp.ui.home.library.detailplaylist
import vvinampapp.composeapp.generated.resources.*

import com.example.vvinamp.util.KmpLogger
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.example.domain.entity.playlist.PlaylistComplete

import com.example.vvinamp.util.PaddingSizeUtil
import com.example.vvinamp.util.orErrorNoText
import com.example.vvinamp.util.preview.previewSafePainter

@Composable
fun PlaylistCreatorComponent(playlist: PlaylistComplete?) {
    val dummyCreators = listOf(playlist?.creator, playlist?.creator)

    Row(
        horizontalArrangement = Arrangement.spacedBy((-8).dp),
        modifier = Modifier
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .zIndex(1f)
                .drawBehind {
                    drawCircle(color = Color.Black, blendMode = BlendMode.Clear)
                }
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.Center),
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        dummyCreators.forEachIndexed { index, dummyCreator ->
            Box(
                modifier = Modifier
                    .zIndex(-index.toFloat())
                    .size(24.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.Black,
                            radius = size.width / 2f + 2.dp.toPx(),
                            blendMode = BlendMode.Clear
                        )
                    }
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                AsyncImage(
                    model = dummyCreator?.profilePicture,
                    contentDescription = null,
                    placeholder = previewSafePainter(Res.drawable.bg_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(PaddingSizeUtil.LibraryItemSize)
                )
            }
        }
    }
}
@Composable
fun PlaylistCreatorRow(playlist: PlaylistComplete?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        PlaylistCreatorComponent(playlist)

        Spacer(Modifier.width(8.dp))

        Text(
            text = playlist?.creator?.username.orEmpty(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TSHeadlineMediumWhite().copy(fontSize = 16.sp)
        )
    }
}
