package com.example.presentation.ui.home.artist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.util.dateToYear
import com.example.domain.entity.song.TopReleasesDomain
import com.example.domain.entity.song.topReleaseDummy
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.ui.widget.ButtonVariableOutlined
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun PopularReleasesContent(topReleasesDomain: TopReleasesDomain) {

    Column(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(R.string.title_popular_releases),
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 17.sp
                )
            )

            Text(
                modifier = Modifier,
                text = stringResource(R.string.label_show_all),
                style = TSHeadlineMediumWhite().copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp
                )
            )
        }

        Spacer(Modifier.height(8.dp))

        topReleasesDomain.albums.take(4).forEach { album ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(80.dp),
                    painter = rememberAsyncImagePainter(
                        model = album.coverArt,
                        placeholder = painterResource(R.drawable.bg_image),
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 12.dp)
                ) {
                    Text(
                        modifier = Modifier,
                        text = album.title,
                        style = TSHeadlineMediumWhite().copy(
                            fontSize = 17.sp
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        modifier = Modifier,
                        text = album.releaseDate.dateToYear(),
                        style = TSHeadlineMediumWhite().copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ButtonVariableOutlined(
                text = "See discography",
                buttonHeight = 25.dp,
                textStyle = TSDisplayMediumWhite().copy(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontWeight = FontWeight.Bold
                ),
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
fun PopularReleasesContentPreview() {
    PreviewTheme {
        PopularReleasesContent(topReleaseDummy)
    }
}