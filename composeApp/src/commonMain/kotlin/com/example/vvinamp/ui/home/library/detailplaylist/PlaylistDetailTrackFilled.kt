package com.example.vvinamp.ui.home.library.detailplaylist
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.dummyPlaylists

import com.example.vvinamp.navigation.router.AuthRouter
import com.example.vvinamp.ui.auth.AuthPage
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.util.orErrorNoText
import com.example.vvinamp.util.orZero
import com.example.vvinamp.util.toDurationLabel
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.navigatorDummy

@Composable
fun PlaylistDetailTrackFilled(
    playlist: PlaylistComplete?,
    titleModifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = titleModifier,
            text = playlist?.title.orErrorNoText(),
            style = TSHeadlineMediumWhite().copy(
                fontSize = 22.sp
            )
        )

        Spacer(Modifier.height(8.dp))

        PlaylistCreatorRow(playlist)

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (playlist?.isPublic == false) Icons.Default.Lock else Icons.Default.Public,
                contentDescription = if (playlist?.isPublic == false) "Private playlist" else "Public playlist",
                tint = MaterialTheme.colorScheme.surfaceBright
            )

            Spacer(Modifier.width(8.dp))

            Text(
                modifier = Modifier,
                // totalDuration is in seconds.
                text = playlist?.totalDuration.orZero().toDurationLabel(),
                style = TSDisplayMediumWhite().copy(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.surfaceBright
                )
            )
        }
    }
}

@Preview
@Composable
private fun PlaylistDetailTrackFilledPreview() {
    PreviewTheme {
        PlaylistDetailTrackFilled(
            playlist = dummyPlaylists.first()
        )
    }
}


