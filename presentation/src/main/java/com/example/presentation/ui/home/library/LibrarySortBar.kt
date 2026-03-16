package com.example.presentation.ui.home.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite


@Composable
fun LibrarySortBar(
    onEvent: (LibraryUiEvent) -> Unit,
    sortBy: String = PlaylistSortOrderDomain.RECENTS.label,
    isLibraryViewModeGrid: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable {
                onEvent(LibraryUiEvent.onSortByClicked)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                painter = painterResource(R.drawable.ic_arrow_up_down),
                contentDescription = null,
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text = sortBy,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 12.sp
                )
            )
        }

        IconButton(
            onClick = { onEvent(LibraryUiEvent.onLibraryViewModesClicked) }
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                painter = if (isLibraryViewModeGrid) painterResource(R.drawable.ic_grid)
                else painterResource(R.drawable.ic_paragraph),
                contentDescription = null,
            )
        }
    }
}