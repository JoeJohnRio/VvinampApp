package com.example.presentation.ui.home.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.domain.entity.ListeningHistoryDomain
import com.example.presentation.ui.home.HomeUiEvent


@Composable
fun ListeningHistoryGrid(
    historyList: List<ListeningHistoryDomain> = emptyList(),
    onEvent: (HomeUiEvent) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(historyList) { history ->
            ListeningHistoryCard(
                history = history,
                onEvent = onEvent
            )
        }
    }
}
