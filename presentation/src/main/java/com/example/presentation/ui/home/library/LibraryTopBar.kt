package com.example.presentation.ui.home.library

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.playlist.LibraryCategory
import com.example.domain.entity.playlist.LibraryCategory.Companion.toLibraryCategory
import com.example.presentation.R
import com.example.presentation.entities.ButtonData
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.widget.ButtonListWithState
import com.example.presentation.ui.widget.TopNavigationWithButtons
import com.example.presentation.util.preview.PreviewTheme


@Composable
fun LibraryTopBar(onEvent: (LibraryUiEvent) -> Unit) {
    Column(
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.background
        )
            .dropShadow(
                shape = RectangleShape,
                shadow = Shadow(
                    radius = 8.dp,
                    spread = 2.dp,
                    color = Color(0x40000000),
                    offset = DpOffset(x = 4.dp, 4.dp)
                )
            )
    ) {
        TopNavigationWithButtons(
            title = stringResource(R.string.title_your_library),
            listOfButtons = listOf(
                ButtonData(
                    iconRes = R.drawable.ic_search,
                    onClick = {
                        onEvent(LibraryUiEvent.onSearchClicked)
                    }
                ),
                ButtonData(
                    iconRes = R.drawable.ic_create_plus,
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

@Preview(showBackground = true)
@Composable
fun LibraryTopBarPreview() {
    PreviewTheme {
        LibraryTopBar({})
    }
}
