package com.example.presentation.ui.home.library.detailplaylist

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.dummyPlaylists
import com.example.presentation.R
import com.example.presentation.navigation.router.AuthRouter
import com.example.presentation.ui.auth.AuthPage
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.util.orErrorNoText
import com.example.presentation.util.preview.PreviewTheme
import com.example.presentation.util.preview.navigatorDummy

@Composable
fun PlaylistDetailTrackFilled(
    playlist: PlaylistComplete?
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier,
            text = playlist?.title.orErrorNoText(),
            style = TSHeadlineMediumWhite().copy(
                fontSize = 22.sp
            )
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            PlaylistCreatorComponent(playlist)

            Spacer(Modifier.width(8.dp))

            Text(
                modifier = Modifier,
                text = playlist?.creator?.username.orErrorNoText(),
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 16.sp
                )
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_public),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceBright
            )

            Spacer(Modifier.width(8.dp))

            Text(
                modifier = Modifier,
                text = "${playlist?.totalDuration}min" ,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.surfaceBright
                )
            )
        }
    }
}

@Preview(apiLevel = 36)
@Composable
private fun PlaylistDetailTrackFilledPreview() {
    PreviewTheme {
        PlaylistDetailTrackFilled(
            playlist = dummyPlaylists.first()
        )
    }
}


