package com.example.vvinamp.ui.search.component
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import com.example.vvinamp.util.preview.previewSafeString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.domain.entity.search.StartBrowsingDomain

import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.previewSafePainter

@Composable
fun StartBrowsingComponent() {
    // Covers from the app's own catalogue, so every card shows music that is actually in the app.
    val listStartBrowsing = listOf(
        StartBrowsingDomain(
            title = "Music",
            idPlaylist = "1",
            imageUrl = "https://bumcqqhbtmmrkeiwlsyy.supabase.co/storage/v1/object/public/media/artist/charlie_puth/album/charlie/charlie.jpg",
            colorHex = 0xffdb148b
        ),
        StartBrowsingDomain(
            title = "Indonesian Pop",
            idPlaylist = "1",
            imageUrl = "https://bumcqqhbtmmrkeiwlsyy.supabase.co/storage/v1/object/public/media/artist/bernadya/album/sialnya_hidup_harus_tetap_berjalan/sialnya_hidup_harus_tetap_berjalan.jpg",
            colorHex = 0xff016450
        ),
        StartBrowsingDomain(
            title = "Throwback",
            idPlaylist = "1",
            imageUrl = "https://bumcqqhbtmmrkeiwlsyy.supabase.co/storage/v1/object/public/media/artist/st12/album/p_u_s_p_a/p_u_s_p_a.jpg",
            colorHex = 0xff8400e7
        ),
        StartBrowsingDomain(
            title = "K-Pop",
            idPlaylist = "1",
            imageUrl = "https://bumcqqhbtmmrkeiwlsyy.supabase.co/storage/v1/object/public/media/artist/treasure/album/last_night/last_night.jpg",
            colorHex = 0xff2d46ba
        ),
    )



    Text(
        text = previewSafeString(Res.string.label_start_browsing, "Start browsing"),
        style = TSDisplayMediumWhite().copy(
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.inversePrimary
        )
    )

    Spacer(Modifier.height(16.dp))

    FlowRow(
        maxItemsInEachRow = 2, // Forces 2 items per row
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        listStartBrowsing.forEach { browsingCategory ->
            CategoryCard(
                title = browsingCategory.title,
                backgroundColor = Color(browsingCategory.colorHex),
                imageUrl = browsingCategory.imageUrl,
            )
        }
    }
}

@Composable
fun FlowRowScope.CategoryCard(
    title: String,
    backgroundColor: Color,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .weight(1f)
            .height(80.dp)
            .clip(RoundedCornerShape(6.dp)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // 1. The Title
            Text(
                text = title,
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 16.sp
                ), // Bold, clear text
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth(0.7f)
                    .padding(
                        start = 12.dp,
                        top = 16.dp
                    )
            )

            // 2. The Rotated Image
            Image(
                painter = rememberAsyncImagePainter(
                    model = imageUrl,
                    placeholder = previewSafePainter(Res.drawable.bg_image),
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp) // Fixed size for the cover art
                    .align(Alignment.BottomEnd) // Pin to bottom-right
                    .offset(x = 10.dp, y = 25.dp) // Push it slightly off-screen
                    .graphicsLayer {
                        rotationZ = 25f // The tilt effect
                        shadowElevation = 8.dp.toPx() // Optional: adds depth
                    }
                    .clip(RoundedCornerShape(6.dp))
            )
        }
    }
}

@Preview
@Composable
fun StartBrowsingComponentPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            StartBrowsingComponent()
        }
    }
}
