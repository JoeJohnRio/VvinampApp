package com.example.presentation.ui.artist

import com.example.domain.usecase.HistoryUseCase
import com.example.domain.util.NetworkMonitor
import com.example.presentation.base.BaseViewModel
import com.example.presentation.ui.home.usecase.GetMoviesWithSeparators
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ArtistViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    getMoviesWithSeparators: GetMoviesWithSeparators,
    val historyUseCase: HistoryUseCase
): BaseViewModel() {

}
