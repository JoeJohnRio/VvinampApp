package com.example.presentation.ui.navigationbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.widget.text.IconWithTextOnly
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun TrackOptionsContent(
    onEvent: (PlaybackUiEvent) -> Unit
) {
    Column(
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        )
    ) {
        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_share),
            text = "Share"
        ) { }

        Spacer(Modifier.height(16.dp))

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_plus_circle),
            text = "Add to playlist"
        ) { }

        Spacer(Modifier.height(16.dp))

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_close),
            text = "Hide in this playlist"
        ) { }

        Spacer(Modifier.height(16.dp))
        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_share),
            text = "Share"
        ) { }

        Spacer(Modifier.height(16.dp))

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_plus_circle),
            text = "Add to playlist"
        ) { }

        Spacer(Modifier.height(16.dp))

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_close),
            text = "Hide in this playlist"
        ) { }

        Spacer(Modifier.height(16.dp))

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_share),
            text = "Share"
        ) { }

        Spacer(Modifier.height(16.dp))

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_plus_circle),
            text = "Add to playlist"
        ) { }

        Spacer(Modifier.height(16.dp))

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_close),
            text = "Hide in this playlist"
        ) { }

        Spacer(Modifier.height(16.dp))

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_share),
            text = "Share"
        ) { }

        Spacer(Modifier.height(16.dp))

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_plus_circle),
            text = "Add to playlist"
        ) { }

        Spacer(Modifier.height(16.dp))

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_close),
            text = "Hide in this playlist"
        ) { }

        Spacer(Modifier.height(16.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Track Option Bottom Sheet",
    showBackground = true
)
@Composable
fun TrackOptionsContentPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            TrackOptionsContent(
                onEvent = {

                }
            )
        }
    }
}