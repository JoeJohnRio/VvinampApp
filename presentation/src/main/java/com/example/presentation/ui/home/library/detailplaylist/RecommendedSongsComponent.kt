package com.example.presentation.ui.home.library.detailplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.domain.entity.album.dummyAlbumDomain
import com.example.domain.entity.artist.artistProfileDummy
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun RecommendedSongsComponent() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.title_recommended_songs),
            style = TSHeadlineMediumWhite().copy(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.inversePrimary
            )
        )

        Spacer(Modifier.height(16.dp))

        ItemRecommendedSong()


    }
}

@Composable
fun ItemRecommendedSong(
    songTitle: String = "Song Title",
    artistName: String = "Artist Name"
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = artistProfileDummy.profileImage,
            contentDescription = null,
            placeholder = painterResource(R.drawable.bg_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = songTitle,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            )

            Spacer(Modifier.hei
                (8.dp))

            Text(
                text = artistName,
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendedSongsComponentPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            RecommendedSongsComponent()
        }
    }
}