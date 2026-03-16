package com.example.presentation.ui.home.library

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.playlist.LibraryCategory
import com.example.domain.entity.playlist.LibraryCategory.Companion.toLibraryCategory
import com.example.presentation.R
import com.example.presentation.entities.ButtonData
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.widget.ButtonListWithState
import com.example.presentation.ui.widget.TopNavigationWithButtons


@Composable
fun LibraryTopBar(onEvent: (LibraryUiEvent) -> Unit) {
    Column() {
        TopNavigationWithButtons(
            title = stringResource(R.string.title_your_library),
            listOfButtons = listOf(
                ButtonData(
                    iconRes = R.drawable.ic_search,
                    onClick = {
                        Log.d("joel123", "search clicked")
                        onEvent(LibraryUiEvent.onSearchClicked)
                    }
                ),
                ButtonData(
                    iconRes = R.drawable.ic_create_plus,
                    onClick = {

                        Log.d("joel123", "oncreate clicked")
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