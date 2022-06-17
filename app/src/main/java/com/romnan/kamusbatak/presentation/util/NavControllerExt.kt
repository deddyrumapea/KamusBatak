package com.romnan.kamusbatak.presentation.util

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

fun NavController.asNavigator() = object : DestinationsNavigator {
    override fun clearBackStack(route: String): Boolean {
        return this@asNavigator.clearBackStack(route = route)
    }

    override fun navigate(
        route: String,
        onlyIfResumed: Boolean,
        builder: NavOptionsBuilder.() -> Unit
    ) {
        return this@asNavigator.navigate(
            route = route,
            builder = builder,
        )
    }

    override fun navigateUp(): Boolean {
        return this@asNavigator.navigateUp()
    }

    override fun popBackStack(): Boolean {
        return this@asNavigator.popBackStack()
    }

    override fun popBackStack(
        route: String,
        inclusive: Boolean,
        saveState: Boolean
    ): Boolean {
        return this@asNavigator.popBackStack(
            route = route,
            inclusive = inclusive,
            saveState = saveState
        )
    }
}