package com.example.presentation.ui.home.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.domain.entity.ListeningHistoryDomain
import com.example.presentation.ui.home.HomeUiEvent
import com.example.presentation.ui.home.HomeViewModel
import com.example.presentation.ui.home.artist.ArtistEvent
import com.example.presentation.ui.home.library.widget.image.PlaylistCover
import com.example.presentation.ui.theme.TSBodyMediumWhite
import com.example.presentation.ui.widget.ImageSize
import com.example.presentation.util.PaddingSizeUtil

@Composable
fun ListeningHistoryCard(
    history: ListeningHistoryDomain,
    modifier: Modifier = Modifier,
    onEvent: (HomeUiEvent) -> Unit
) {
    Card(
        modifier = modifier.clickable {
            onEvent(HomeUiEvent.OnHistoryClicked(history))
        },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PlaylistCover(
                coverImage = "",
                covers = history.contentImage,
                modifier = Modifier
                    .size(PaddingSizeUtil.ListeningHistoryPhotoSize)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = history.contentName,
                    style = TSBodyMediumWhite().copy(
                        fontSize = 11.sp
                    )
                )

                if (history.progress > 0 && history.progress < 100) {
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = history.progress / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color.Green
                    )
                }
            }
        }
    }
}