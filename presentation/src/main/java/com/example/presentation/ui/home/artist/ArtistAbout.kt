package com.example.presentation.ui.home.artist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.domain.entity.artist.ArtistProfileDomain
import com.example.domain.entity.artist.artistProfileDummy
import com.example.presentation.R
import com.example.presentation.ui.theme.TSBodyMediumWhite
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSLabelMediumWhite
import com.example.presentation.util.addCommas
import com.example.presentation.util.orErrorNoText
import com.example.presentation.util.preview.PreviewTheme
import com.example.presentation.util.verticalGradientEdge

@Composable
fun ArtistAbout(artistProfile: ArtistProfileDomain) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            text = "About",
            style = TSBodyMediumWhite().copy(
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.inversePrimary,
            )
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .aspectRatio(1f)
                    .verticalGradientEdge(Color.Black),
                painter = rememberAsyncImagePainter(
                    model = artistProfile.profileImage,
                    placeholder = painterResource(R.drawable.bg_image),
                ),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )

            Column(
                modifier = Modifier
                    .align(alignment = Alignment.BottomStart)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 20.dp
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = artistProfile.monthlyListeners.addCommas(),
                        style = TSBodyMediumWhite().copy(
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.inversePrimary,
                        )
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = "MONTHLY LISTENERS",
                        style = TSLabelMediumWhite().copy(
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.inversePrimary,
                        )
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = artistProfile.bio.orErrorNoText(),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.inversePrimary,
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun ArtistAboutPreview() {
    PreviewTheme {
        ArtistAbout(artistProfileDummy)
    }
}