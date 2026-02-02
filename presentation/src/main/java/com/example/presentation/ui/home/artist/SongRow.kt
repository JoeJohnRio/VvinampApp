package com.example.presentation.ui.home.artist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.song.topSongsDummy
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun SongRow(
    song: TrackDomain,
    index: Int,
    imageUrlJoel123: String,
    isCurrent: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${index + 1}",
            color = if (isCurrent) Color(0xFF1DB954) else Color.Gray,
            modifier = Modifier.width(30.dp),
            style = TSDisplayMediumWhite().copy(
                fontSize = 16.sp
            )
        )

        Image(
            painter = rememberAsyncImagePainter(
                model = imageUrlJoel123,
                placeholder = painterResource(R.drawable.bg_image),
            ),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 16.sp,
                    color = if (isCurrent) Color(0xFF1DB954) else Color.White,
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = song.streamCount.toString(),
                color = Color.Gray,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 12.sp,
                    color = Color.Gray,
                )
            )
        }

        if (index == 1) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF1DB954),
                modifier = Modifier
                    .size(20.dp)
                    .padding(horizontal = 4.dp)
            )
        }

        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Preview
@Composable
fun SongRowPreview() {
    val topSongs = topSongsDummy

    PreviewTheme {
        LazyColumn() {
            itemsIndexed(topSongs?.tracks.orEmpty()) { index, song ->
                SongRow(
                    song = song,
                    index = index,
                    imageUrlJoel123 = "https://cirio.my.id/images/artists/sheilaOn7_artist_profile.jpg",
                )
            }
        }
    }
}
