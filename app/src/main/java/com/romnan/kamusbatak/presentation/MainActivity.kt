package com.romnan.kamusbatak.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.navigateTo
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.romnan.kamusbatak.domain.model.ThemeMode
import com.romnan.kamusbatak.presentation.bookmarks.BookmarksScreen
import com.romnan.kamusbatak.presentation.components.NavigationDrawerContent
import com.romnan.kamusbatak.presentation.destinations.*
import com.romnan.kamusbatak.presentation.entriesFinder.EntriesFinderScreen
import com.romnan.kamusbatak.presentation.partuturan.PartuturanScreen
import com.romnan.kamusbatak.presentation.preferences.PreferencesScreen
import com.romnan.kamusbatak.presentation.quizGame.QuizGamesScreen
import com.romnan.kamusbatak.presentation.theme.KamusBatakTheme
import com.romnan.kamusbatak.presentation.umpasa.UmpasaCategoryScreen
import com.romnan.kamusbatak.presentation.util.asNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.themeMode.value == null }
        }
        setContent {
            val engine = rememberNavHostEngine()
            val navController = engine.rememberNavController()
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()

            val themeMode = viewModel.themeMode.collectAsState().value

            KamusBatakTheme(
                darkTheme = when (themeMode) {
                    ThemeMode.System -> isSystemInDarkTheme()
                    ThemeMode.Light -> false
                    ThemeMode.Dark -> true
                    null -> isSystemInDarkTheme()
                }
            ) {
                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                    drawerContent = {
                        NavigationDrawerContent(
                            currentDestination = {
                                navController.appCurrentDestinationAsState().value
                                    ?: NavGraphs.root.startAppDestination
                            },
                            onItemClick = { direction: DirectionDestinationSpec ->
                                navController.navigateTo(direction) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                scope.launch { scaffoldState.drawerState.close() }
                            },
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

                        composable(QuizGamesScreenDestination) {
                            QuizGamesScreen(
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

                        composable(BookmarksScreenDestination) {
                            BookmarksScreen(
                                navigator = navController.asNavigator(),
                                parentScaffoldState = scaffoldState,
                            )
                        }

                        composable(PartuturanScreenDestination) {
                            PartuturanScreen(
                                navigator = navController.asNavigator(),
                                parentScaffoldState = scaffoldState,
                            )
                        }

                        composable(UmpasaCategoryScreenDestination) {
                            UmpasaCategoryScreen(
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