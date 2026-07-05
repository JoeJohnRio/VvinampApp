package com.example.vvinamp.navigation

import androidx.navigation3.runtime.NavKey
import kotlin.reflect.KClass

/**
 * Handles navigation events (forward and back) by updating the navigation state.
 */
class Navigator(val state: NavigationState){
    fun navigate(route: NavKey){
        if (route in state.backStacks.keys){
            // This is a top level route, just switch to it.
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack(){
        val currentStack = state.backStacks[state.topLevelRoute] ?:
        error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        // If we're at the base of the current route, go back to the start route stack.
        if (currentRoute == state.topLevelRoute){
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }

    /**
     * Pops a specific number of pages from the current backstack.
     * @param count Number of pages to remove from the top of the stack
     */
    fun popPages(count: Int) {
        val currentStack = state.backStacks[state.topLevelRoute] ?:
        error("Stack for ${state.topLevelRoute} not found")

        repeat(count) {
            if (currentStack.size > 1) { // Keep at least the base route
                val currentRoute = currentStack.last()
                if (currentRoute == state.topLevelRoute) {
                    state.topLevelRoute = state.startRoute
                    return
                } else {
                    currentStack.removeLastOrNull()
                }
            }
        }
    }

    /**
     * Pops pages until reaching a specific route (exclusive - target route stays).
     * @param targetRoute The route to pop back to (this route will remain in the stack)
     */
    fun popUntil(targetRoute: NavKey) {
        val currentStack = state.backStacks[state.topLevelRoute] ?:
        error("Stack for ${state.topLevelRoute} not found")

        while (currentStack.lastOrNull() != targetRoute && currentStack.size > 1) {
            currentStack.removeLastOrNull()
        }

        // If we didn't find the target route, check if it's a top-level route
        if (currentStack.lastOrNull() != targetRoute && targetRoute in state.backStacks.keys) {
            state.topLevelRoute = targetRoute
        }
    }

}