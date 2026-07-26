package com.example.vvinamp.ui.home.library
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vvinamp.util.preview.previewSafeString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.playlist.LibraryCategory
import com.example.domain.entity.playlist.LibraryCategory.Companion.toLibraryCategory
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.widget.ButtonListWithState
import com.example.vvinamp.ui.widget.TopNavigationWithButtons
import com.example.vvinamp.util.ButtonData
import com.example.vvinamp.util.preview.PreviewTheme


@Composable
fun LibraryTopBar(onEvent: (LibraryUiEvent) -> Unit) {
    Column(
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.background
        )
    ) {
        TopNavigationWithButtons(
            title = previewSafeString(Res.string.title_your_library, "Your Library"),
            listOfButtons = listOf(
                ButtonData(
                    imageVector = Icons.Default.Search,
                    onClick = {
                        onEvent(LibraryUiEvent.onSearchClicked)
                    }
                ),
                ButtonData(
                    imageVector = Icons.Default.Add,
                    onClick = {
                        onEvent(LibraryUiEvent.onCreateClicked)
                    }
                )
            )
        )

        Spacer(Modifier.height(12.dp))

        ButtonListWithState(
            modifier = Modifier
                .fillMaxWidth(),
            buttonHorizontalPadding = 12.dp,
            buttonHeight = 32.dp,
            textStyle = TSDisplayMediumWhite().copy(
                fontSize = 11.sp
            ),
            listButton = listOf(
                LibraryCategory.PLAYLIST.displayName,
                LibraryCategory.PODCAST.displayName,
                LibraryCategory.ALBUM.displayName,
                LibraryCategory.ARTIST.displayName,
                LibraryCategory.DOWNLOADED.displayName,
            ),
            outlineColor = Color.Transparent,
            textColor = MaterialTheme.colorScheme.inversePrimary,
            textColorClicked = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            containerColorClicked = MaterialTheme.colorScheme.primary,
            onClick = { gender ->
                onEvent(LibraryUiEvent.onLibraryCategoryClicked(gender.toLibraryCategory()))
            }
        )
    }
}

@Preview
@Composable
fun LibraryTopBarPreview() {
    PreviewTheme {
        LibraryTopBar({})
    }
}
