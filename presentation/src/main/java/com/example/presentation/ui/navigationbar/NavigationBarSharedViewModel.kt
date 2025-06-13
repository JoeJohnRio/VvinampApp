package com.example.youtubemusicclone.ui.navigationbar

import com.example.youtubemusicclone.base.BaseViewModel
import com.example.youtubemusicclone.util.singleSharedFlowJoel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class NavigationBarSharedViewModel @Inject constructor() : BaseViewModel() {

    private val _bottomItem = singleSharedFlowJoel<BottomNavigationBarItem>()
    val bottomItem = _bottomItem.asSharedFlow()

    fun onBottomItemClicked(bottomItem: BottomNavigationBarItem) = launch {
        _bottomItem.emit(bottomItem)
    }
}