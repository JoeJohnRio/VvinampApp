package com.example.vvinamp.ui.home.library.detailplaylist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import com.example.vvinamp.ui.theme.PhoneColumnMaxWidth
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.playlist.PlaylistTrackSortByDomain
import com.example.vvinamp.ui.home.library.SortOptionRow
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistTrackSortBottomSheet(
    currentSort: PlaylistTrackSortByDomain,
    onSortSelected: (PlaylistTrackSortByDomain) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        sheetMaxWidth = PhoneColumnMaxWidth,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Sort by",
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )

            HorizontalDivider(
                Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.surfaceContainer
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                PlaylistTrackSortByDomain.entries.forEach { option ->
                    SortOptionRow(
                        label = option.label,
                        isSelected = option == currentSort,
                        onClick = {
                            onSortSelected(option)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}
