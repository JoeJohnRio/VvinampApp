package com.example.presentation.ui.search.searchresult

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.entity.search.dummySearchItems
import com.example.presentation.R
import com.example.presentation.ui.theme.TSLabelMediumWhite
import com.example.presentation.util.preview.PreviewTheme


@Composable
fun SearchItemComponent(domain: SearchItemDomain?) {
    when (domain) {
        is SearchItemDomain.TrackSearch -> {
            TrackSearchComponent(domain)
        }
//        else -> {
//            Text(
//                "Item: ${domain}"
//            )
//        }
        is SearchItemDomain.AlbumSearch -> Text(
            "Item 1: ${domain}"
        )

        is SearchItemDomain.ArtistSearch -> Text(
            "Item 2: ${domain}"
        )

        is SearchItemDomain.PlaylistSearch -> Text(
            "Item 3: ${domain}"
        )

        is SearchItemDomain.PodcastEpisodeSearch -> Text(
            "Item 4: ${domain}"
        )

        is SearchItemDomain.PodcastSeriesSearch -> Text(
            "Item 5: ${domain}"
        )

        null -> Text(
            "Item 6: ${domain}"
        )
    }
}

@Composable
fun TrackSearchComponent(domain: SearchItemDomain.TrackSearch) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row() {
            Image(
                painter = rememberAsyncImagePainter(
                    model = domain.albumCoverUrl,
                    placeholder = painterResource(R.drawable.bg_image),
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentScale = ContentScale.Crop
            )

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = domain.title,
                    style = TSLabelMediumWhite().copy(
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                    )
                )

                Text(
                    text = "Song • ${domain.artistName}",
                    style = TSLabelMediumWhite().copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                )
            }
        }

        Row() {
            IconButton(
                onClick = {
                    //add to liked song
                },
                modifier = Modifier.size(48.dp) // Standard IconButton size
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_three_dots),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(
                onClick = {
                    //add to liked song
                },
                modifier = Modifier.size(48.dp) // Standard IconButton size
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_plus_circle),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchItemComponentPreview() {
    PreviewTheme {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)) {
            dummySearchItems.forEach {
                SearchItemComponent(
                    domain = it
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}