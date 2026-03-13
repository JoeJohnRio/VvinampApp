package com.example.presentation.ui.home.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.playlistMinimalDummy
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.util.PaddingUtil
import com.example.presentation.util.preview.PreviewContainer

@Composable
fun LibraryItem(
    playlistMinimal: PlaylistMinimal
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = playlistMinimal.coverImage,
            contentDescription = null,
            placeholder = painterResource(R.drawable.bg_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(PaddingUtil.LibraryItemSize)
        )

        Spacer(Modifier.width(12.dp))

        Column() {
            Text(
                text = playlistMinimal.title,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 15.sp
                )
            )
            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (true) {
                    Icon(
                        painter = painterResource(R.drawable.ic_pin),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null,
                    )

                    Spacer(Modifier.width(4.dp))
                }

                Text(
                    text = "Playlist • ${playlistMinimal.username}",
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.surfaceBright
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryItemPreview() {
    PreviewContainer {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            LibraryItem(playlistMinimalDummy)
        }
    }
}