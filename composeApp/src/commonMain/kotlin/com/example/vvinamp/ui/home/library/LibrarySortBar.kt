package com.example.vvinamp.ui.home.library
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.playlist.PlaylistSortOrderDomain

import com.example.vvinamp.ui.theme.TSDisplayMediumWhite


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
                imageVector = Icons.Default.SwapVert,
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
                imageVector = if (isLibraryViewModeGrid) Icons.Default.GridView
                else Icons.AutoMirrored.Filled.Notes,
                contentDescription = null,
            )
        }
    }
}