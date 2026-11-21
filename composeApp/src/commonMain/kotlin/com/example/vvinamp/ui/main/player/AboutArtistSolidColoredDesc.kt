package com.example.vvinamp.ui.main.player
import androidx.compose.foundation.clickable
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
import com.example.vvinamp.util.preview.previewSafeString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.domain.entity.song.TrackDomain

import com.example.vvinamp.ui.main.playback.PlaybackUiState
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.ui.widget.ButtonVariableOutlined
import com.example.vvinamp.ui.widget.text.ExpandableText
import com.example.vvinamp.util.ListenerUtils
import com.example.vvinamp.util.orErrorNoText
import com.example.vvinamp.util.preview.previewSafePainter

fun resolveArtistImageUrl(track: TrackDomain?): String {
    if (track == null) return ""
    val explicitImage = track.artistProfileImageUrl
    if (!explicitImage.isNullOrBlank()) {
        return explicitImage
    }

    val albumCover = track.albumCoverUrl
    val artistMatch = Regex("""(.+/media/artist/([^/]+))/album/.*""").find(albumCover)
    if (artistMatch != null) {
        val baseArtistPath = artistMatch.groupValues[1]
        val artistSlug = artistMatch.groupValues[2]
        return "$baseArtistPath/$artistSlug.jpg"
    }

    return albumCover
}

@Composable
fun AboutArtistSolidColoredDesc(
    uiState: PlaybackUiState,
    onOpenArtist: (String) -> Unit = {}
) {
    val currentTrack = uiState.currentTrack
    val artistImageUrl = resolveArtistImageUrl(currentTrack)
    val fallbackImageUrl = currentTrack?.albumCoverUrl.orEmpty()

    val artistId = currentTrack?.artistId
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .then(if (artistId != null) Modifier.clickable { onOpenArtist(artistId) } else Modifier)
    ) {
        ImageAboutArtist(
            artistImageUrl = artistImageUrl,
            fallbackImageUrl = fallbackImageUrl
        )

        ArtistAboutDetail(currentTrack)
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

                val listeners = currentTrack?.artistMonthlyListeners
                val streamCount = currentTrack?.streamCount
                val monthlyListenersText = when {
                    listeners != null && listeners > 0L -> {
                        "${ListenerUtils.formatCompact(listeners)} monthly listeners"
                    }
                    streamCount != null && streamCount > 0 -> {
                        "${ListenerUtils.formatCompact(streamCount.toLong())} monthly listeners"
                    }
                    else -> "Verified Artist"
                }

                Text(
                    text = monthlyListenersText,
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

        val explicitBio = currentTrack?.artistBio
        val bioText = if (!explicitBio.isNullOrBlank()) {
            explicitBio
        } else {
            currentTrack?.artistName?.takeIf { it.isNotBlank() }?.let {
                "$it is a featured recording artist on Vvinamp."
            } ?: "Discover music and releases on Vvinamp."
        }

        ExpandableText(
            text = bioText,
            textStyle = TSDisplayMediumWhite().copy(
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.surfaceBright
            )
        )
    }
}

@Composable
fun ImageAboutArtist(
    artistImageUrl: String = "",
    fallbackImageUrl: String = ""
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
    ) {

        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = artistImageUrl.ifBlank { fallbackImageUrl },
            contentDescription = null,
            placeholder = previewSafePainter(Res.drawable.bg_image),
            error = previewSafePainter(Res.drawable.bg_image),
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
            text = previewSafeString(Res.string.title_about_the_artist, "About the artist"),
            style = TSHeadlineMediumWhite().copy(
                fontSize = 13.sp
            ),
        )

    }
}
