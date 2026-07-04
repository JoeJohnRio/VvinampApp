package com.example.vvinamp.ui.home.library
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.util.preview.PreviewTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistBottomSheet(
    onEvent: (LibraryUiEvent) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // Keeps it tight to the content
    )

    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() } // The "draggable" visual cue
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ActionMenuItem(
                title = "Playlist",
                description = "Create a playlist with songs or episodes",
                icon = painterResource(Res.drawable.ic_music_note),
                onClick = {
                    onEvent(LibraryUiEvent.onCreateNewPlaylistClicked)
                }
            )

            Spacer(Modifier.height(16.dp))

            ActionMenuItem(
                title = "Collaborative playlist",
                description = "create a playlist together with friends",
                icon = painterResource(Res.drawable.ic_collaboration_group),
                onClick = {

                }
            )

            Spacer(Modifier.height(16.dp))

            ActionMenuItem(
                title = "Blend",
                description = "Combine your frineds' tastes into a playlist",
                icon = painterResource(Res.drawable.ic_venn),
                onClick = {

                }
            )
        }
    }
}

@Composable
fun ActionMenuItem(
    title: String,
    description: String,
    icon: Painter,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.clickable{
            onClick()
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = CircleShape
                )
        ) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp)
                    .align(Alignment.Center),
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceBright
            )
        }

        Spacer(Modifier.width(16.dp))
        Column(

        ) {
            Text(
                text = title,
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 16.sp
                )
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = description,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.surfaceBright
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePlaylistBottomSheetPreview() {
    PreviewTheme {
        CreatePlaylistBottomSheet(
            onEvent = {},
            onDismiss = {}
        )
    }
}