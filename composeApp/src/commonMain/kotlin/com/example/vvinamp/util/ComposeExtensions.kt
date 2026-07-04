package com.example.vvinamp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import com.example.vvinamp.navigation.NavigationState

//@Composable
//inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
//    navigationState: NavigationState,
//): T {
//    val navGraphRoute = destination.parent?.route ?: return koinViewModel()
//
//    val parentEntry = remember(this) {
//        navigationState.getBackStackEntry(route = navGraphRoute)
//    }
//    return hiltViewModel(parentEntry)
//}