package com.example.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import com.example.presentation.navigation.NavigationState

//@Composable
//inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
//    navigationState: NavigationState,
//): T {
//    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
//
//    val parentEntry = remember(this) {
//        navigationState.getBackStackEntry(route = navGraphRoute)
//    }
//    return hiltViewModel(parentEntry)
//}