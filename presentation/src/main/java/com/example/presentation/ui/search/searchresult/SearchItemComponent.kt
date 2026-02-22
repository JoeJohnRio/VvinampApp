package com.example.presentation.ui.search.searchresult

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import coil3.compose.AsyncImage
import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.entity.search.dummySearchItems
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSLabelMediumWhite
import com.example.presentation.util.preview.PreviewTheme


@Composable
fun SearchItemComponent(
    domain: SearchItemDomain?,
    onClicked: (SearchItemDomain) -> Unit = {}
) {
    when (domain) {
        is SearchItemDomain.TrackSearch -> TrackSearchComponent(domain, onClicked)

        is SearchItemDomain.AlbumSearch -> AlbumSearchComponent(domain, onClicked)

        is SearchItemDomain.ArtistSearch -> ArtistSearchComponent(domain, onClicked)

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
fun TrackSearchComponent(
    domain: SearchItemDomain.TrackSearch,
    onClicked: (SearchItemDomain.TrackSearch) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClicked(domain) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = domain.albumCoverUrl,
                contentDescription = null,
                placeholder = painterResource(R.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = domain.title,
                    style = TSDisplayMediumWhite().copy(
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

        Row(
            modifier = Modifier.weight(0.3f),
        ) {
            IconButton(
                onClick = {
                    //add to liked song
                },
                modifier = Modifier.size(48.dp) // Standard IconButton size
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bullet_vertical),
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

@Composable
fun AlbumSearchComponent(
    domain: SearchItemDomain.AlbumSearch,
    onClicked: (SearchItemDomain) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClicked(domain)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = domain.coverArt,
                contentDescription = null,
                placeholder = painterResource(R.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = domain.title,
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                    )
                )

                Text(
                    text = "Album • ${domain.artistName}",
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
                    painter = painterResource(R.drawable.ic_plus_circle),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun ArtistSearchComponent(
    domain: SearchItemDomain.ArtistSearch,
    onClicked: (SearchItemDomain) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClicked(domain)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = domain.profileImage,
                contentDescription = null,
                placeholder = painterResource(R.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = domain.name,
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                    )
                )

                Text(
                    text = "Artist",
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
                    painter = painterResource(R.drawable.ic_bullet_vertical),
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            dummySearchItems.forEach {
                SearchItemComponent(it)

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}