package com.romnan.kamusbatak.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.romnan.kamusbatak.NavGraphs
import com.romnan.kamusbatak.core.presentation.components.NavigationDrawerContent
import com.romnan.kamusbatak.core.presentation.theme.KamusBatakTheme
import com.romnan.kamusbatak.core.presentation.util.asNavigator
import com.romnan.kamusbatak.destinations.EntriesFinderScreenDestination
import com.romnan.kamusbatak.destinations.PreferencesScreenDestination
import com.romnan.kamusbatak.featEntriesFinder.presentation.EntriesFinderScreen
import com.romnan.kamusbatak.featPreferences.presentation.PreferencesScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val engine = rememberNavHostEngine()
            val navController = engine.rememberNavController()
            val scaffoldState = rememberScaffoldState()

            KamusBatakTheme {
                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                    drawerContent = {
                        NavigationDrawerContent(
                            navController = navController,
                            drawerState = scaffoldState.drawerState,
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) { scaffoldPadding ->
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        engine = engine,
                        navController = navController,
                        modifier = Modifier.padding(scaffoldPadding),
                    ) {
                        composable(EntriesFinderScreenDestination) {
                            EntriesFinderScreen(
                                navigator = navController.asNavigator(),
                                parentScaffoldState = scaffoldState,
                            )
                        }

                        composable(PreferencesScreenDestination) {
                            PreferencesScreen(
                                navigator = navController.asNavigator(),
                                parentScaffoldState = scaffoldState,
                            )
                        }
                    }
                }
            }
        }
    }
}