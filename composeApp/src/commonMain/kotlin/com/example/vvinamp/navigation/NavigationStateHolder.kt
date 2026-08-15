package com.example.vvinamp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.vvinamp.navigation.graph.AuthDestination
import com.example.vvinamp.navigation.graph.BottomNavDestination
import com.example.vvinamp.navigation.graph.MainDestination
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * Create a navigation state that persists config changes and process death.
 */
@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>
): NavigationState {

    val savedStateConfig = remember(topLevelRoutes) {
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    // AuthDestination
                    subclass(AuthDestination.Auth::class)
                    subclass(AuthDestination.AuthRoot::class)
                    subclass(AuthDestination.RegisterEmail::class)
                    subclass(AuthDestination.RegisterPassword::class)
                    subclass(AuthDestination.RegisterBirthDate::class)
                    subclass(AuthDestination.RegisterGender::class)
                    subclass(AuthDestination.RegisterName::class)
                    subclass(AuthDestination.RegisterMethodSelection::class)
                    subclass(AuthDestination.LoginEmail::class)
                    subclass(AuthDestination.LoginMethodSelection::class)

                    // MainDestination
                    subclass(MainDestination.NavigationBar::class)
                    subclass(MainDestination.Root::class)
                    subclass(MainDestination.Main::class)

                    // BottomNavDestination
                    subclass(BottomNavDestination.Home::class)
                    subclass(BottomNavDestination.Search::class)
                    subclass(BottomNavDestination.Library::class)
                    subclass(BottomNavDestination.CreatePlaylist::class)
                    subclass(BottomNavDestination.PlaylistDetail::class)
                    subclass(BottomNavDestination.PlaylistEdit::class)
                    subclass(BottomNavDestination.SearchResults::class)
                    subclass(BottomNavDestination.Create::class)
                    subclass(BottomNavDestination.Artist::class)
                    subclass(BottomNavDestination.Album::class)
                    subclass(BottomNavDestination.Settings::class)
                }
                polymorphic(AuthDestination::class) {
                    subclass(AuthDestination.Auth::class)
                    subclass(AuthDestination.AuthRoot::class)
                    subclass(AuthDestination.RegisterEmail::class)
                    subclass(AuthDestination.RegisterPassword::class)
                    subclass(AuthDestination.RegisterBirthDate::class)
                    subclass(AuthDestination.RegisterGender::class)
                    subclass(AuthDestination.RegisterName::class)
                    subclass(AuthDestination.RegisterMethodSelection::class)
                    subclass(AuthDestination.LoginEmail::class)
                    subclass(AuthDestination.LoginMethodSelection::class)
                }
                polymorphic(MainDestination::class) {
                    subclass(MainDestination.NavigationBar::class)
                    subclass(MainDestination.Root::class)
                    subclass(MainDestination.Main::class)
                }
                polymorphic(BottomNavDestination::class) {
                    subclass(BottomNavDestination.Home::class)
                    subclass(BottomNavDestination.Search::class)
                    subclass(BottomNavDestination.Library::class)
                    subclass(BottomNavDestination.CreatePlaylist::class)
                    subclass(BottomNavDestination.PlaylistDetail::class)
                    subclass(BottomNavDestination.PlaylistEdit::class)
                    subclass(BottomNavDestination.SearchResults::class)
                    subclass(BottomNavDestination.Create::class)
                    subclass(BottomNavDestination.Artist::class)
                    subclass(BottomNavDestination.Album::class)
                    subclass(BottomNavDestination.Settings::class)
                }
            }
        }
    }

    val topLevelRoute = rememberSerializable(
        startRoute, topLevelRoutes,
        stateSerializer = PolymorphicSerializer(NavKey::class) as KSerializer<NavKey>,
        configuration = savedStateConfig
    ) {
        mutableStateOf(startRoute)
    }

    val backStacks = topLevelRoutes.associateWith { key ->
        rememberNavBackStack(configuration = savedStateConfig, elements = arrayOf(key))
    }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            backStacks = backStacks
        )
    }
}

/**
 * State holder for navigation state.
 *
 * @param startRoute - the start route. The user will exit the app through this route.
 * @param topLevelRoute - the current top level route
 * @param backStacks - the back stacks for each top level route
 */
class NavigationState(
    val startRoute: NavKey,
    topLevelRoute: MutableState<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>
) {
    var topLevelRoute: NavKey by topLevelRoute
    val stacksInUse: List<NavKey>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }
}

/**
 * Convert NavigationState into NavEntries.
 */
@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): SnapshotStateList<NavEntry<NavKey>> {

    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider
        )
    }

    return stacksInUse
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}