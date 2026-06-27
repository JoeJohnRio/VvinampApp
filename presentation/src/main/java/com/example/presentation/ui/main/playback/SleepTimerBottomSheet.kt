package com.example.presentation.ui.main.playback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.ui.home.library.SortOptionRow
import com.example.presentation.ui.theme.TSHeadlineMediumWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimerBottomSheet(
    currentOption: SleepTimerOption?,
    onOptionSelected: (SleepTimerOption) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Sleep timer",
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
                SleepTimerOption.entries.forEach { option ->
                    SortOptionRow(
                        label = option.label,
                        isSelected = option == currentOption,
                    ) {
                        onOptionSelected(option)
                    }
                }
            }
        }
    }
}
