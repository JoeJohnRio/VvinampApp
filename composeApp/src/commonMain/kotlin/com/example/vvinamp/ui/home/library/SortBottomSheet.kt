package com.example.vvinamp.ui.home.library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    currentSort: PlaylistSortOrderDomain,
    onSortSelected: (PlaylistSortOrderDomain) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp) // Extra padding for system navigation bars
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
                PlaylistSortOrderDomain.entries.forEach { option ->
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