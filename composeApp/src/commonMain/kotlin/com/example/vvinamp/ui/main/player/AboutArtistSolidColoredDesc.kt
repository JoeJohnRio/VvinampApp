package com.example.vvinamp.ui.main.player
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.domain.entity.song.TrackDomain

import com.example.vvinamp.ui.main.playback.PlaybackUiState
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.ui.widget.ButtonVariableOutlined
import com.example.vvinamp.ui.widget.text.ExpandableText
import com.example.vvinamp.util.orErrorNoText

@Composable
fun AboutArtistSolidColoredDesc(
    uiState: PlaybackUiState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        ImageAboutArtist(uiState.currentTrack?.albumCoverUrl.orEmpty())

        ArtistAboutDetail(uiState.currentTrack)
    }
}

@Composable
fun ArtistAboutDetail(currentTrack: TrackDomain?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = currentTrack?.artistName.orErrorNoText(),
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 18.sp
                    )
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "13.6M monthly listeners",
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.surfaceBright
                    )
                )
            }

            ButtonVariableOutlined(
                modifier = Modifier
                    .height(30.dp)
                    .align(Alignment.TopEnd),
                text = "Follow",
                textStyle = TSDisplayMediumWhite().copy(
                    fontSize = 12.sp
                )
            )
        }

        Spacer(Modifier.height(16.dp))

        ExpandableText(
            text = "Sheila on 7 is an alternative rock band from Yogyakarta, Indonesia. They have changed their lineup several times since their formation on 6 May 1996. Their first lineup consisted of Akhdiyat Duta Modjo (also known as Duta, vocals), Saktia \"Sakti\" Ari Seno (guitar), Eross Candra (guitar), Adam Muhammad Subarkah (bass), and Anton Widiastanto (drums). Anton left the group in 2004, while Sakti left in 2006, leaving their current lineup as it is, but with Brian Kresna Putro replacing Anton in the drums until his departure in 2022.",
            textStyle = TSDisplayMediumWhite().copy(
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.surfaceBright
            )
        )
    }
}

@Composable
fun ImageAboutArtist(
    artistImageUrl: String = ""
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
    ) {

        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = artistImageUrl,
            contentDescription = null,
            placeholder = painterResource(Res.drawable.bg_image),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                        0.3f to Color.Transparent
                    )
                )
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp),
            text = stringResource(Res.string.title_about_the_artist),
            style = TSHeadlineMediumWhite().copy(
                fontSize = 13.sp
            ),
        )

    }
}
