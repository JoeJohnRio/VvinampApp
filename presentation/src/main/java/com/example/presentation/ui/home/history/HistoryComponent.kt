package com.example.presentation.ui.home.history

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.domain.entity.ListeningHistoryDomain
import com.example.presentation.ui.home.HomeEvent
import com.example.presentation.ui.home.HomeViewModel
import com.example.presentation.ui.theme.TSBodyMediumWhite
import com.example.presentation.ui.widget.ImageSize
import com.example.presentation.ui.widget.NoImagePlaceholder
import com.example.presentation.ui.widget.toPX
import kotlinx.coroutines.delay

@Composable
fun ListeningHistoryCard(
    history: ListeningHistoryDomain,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    Card(
        modifier = modifier.clickable {
            homeViewModel.onEvent(HomeEvent.OnHistoryClicked(history))
        },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HistoryHomeItem(
                imageUrl = history.contentImage,
                imageSize = ImageSize.getImageListeningHistorySize(),
                itemVisible = true
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

@Composable
private fun HistoryHomeItem(
    imageUrl: String,
    imageSize: ImageSize,
    itemVisible: Boolean,
) {
    var scale by remember { mutableFloatStateOf(0.70f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        label = "FloatAnimation"
    )

    LaunchedEffect(itemVisible) {
        if (itemVisible) {
            delay(100)
            scale = 1f
        } else {
            scale = 0.70f
        }
    }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .scale(Scale.FILL)
            .size(imageSize.width.toPX(), imageSize.height.toPX())
            .build(),
        loading = { NoImagePlaceholder() },
        error = { NoImagePlaceholder() },
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(imageSize.width, imageSize.height)
            .scale(animatedScale)
            .clip(RoundedCornerShape(4.dp)) // Optional: Rounded corners
    )
}


@Composable
fun ListeningHistoryGrid(
    historyList: List<ListeningHistoryDomain> = emptyList()
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(historyList) { history ->
            ListeningHistoryCard(history)
        }
    }
}
