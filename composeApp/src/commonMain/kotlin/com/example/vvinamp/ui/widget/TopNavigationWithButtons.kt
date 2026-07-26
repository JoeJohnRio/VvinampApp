package com.example.vvinamp.ui.widget
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.util.ButtonData
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.previewSafePainter


@Composable
fun TopNavigationWithButtons(
    title: String,
    listOfButtons: List<ButtonData> = listOf()
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(32.dp),
                painter = rememberAsyncImagePainter(
                    model = "https://avatars.githubusercontent.com/u/42718154?v=4",
                    placeholder = previewSafePainter(Res.drawable.bg_image),
                ),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )

            Spacer(Modifier.padding(8.dp))

            Text(
                text = title,
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            )
        }

        Row() {
            listOfButtons.forEach { (iconRes, imageVector, onClick) ->
                IconButton(onClick = { onClick() }) {
                    val iconModifier = Modifier

                    if (imageVector != null) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.inversePrimary,
                            modifier = iconModifier
                        )
                    } else if (iconRes != null) {
                        Icon(
                            painter = previewSafePainter(iconRes),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.inversePrimary,
                            modifier = iconModifier
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TopNavigationWithButtonsPreview() {
    PreviewTheme {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            TopNavigationWithButtons(
                title = "Title",
                listOfButtons = listOf(
                    ButtonData(
                        imageVector = Icons.Default.Search,
                        onClick = {}
                    ),
                    ButtonData(
                        imageVector = Icons.Default.Add,
                        onClick = {}
                    )
                )
            )
        }
    }
}
