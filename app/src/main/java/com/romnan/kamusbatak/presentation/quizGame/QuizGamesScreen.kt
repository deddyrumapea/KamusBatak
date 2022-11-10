package com.romnan.kamusbatak.presentation.quizGame

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.QuizGame
import com.romnan.kamusbatak.presentation.components.DefaultTopBar
import com.romnan.kamusbatak.presentation.destinations.QuizPlayingScreenDestination
import com.romnan.kamusbatak.presentation.theme.spacing
import com.romnan.kamusbatak.presentation.util.asString
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination
fun QuizGamesScreen(
    navigator: DestinationsNavigator,
    parentScaffoldState: ScaffoldState,
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            DefaultTopBar(
                title = { stringResource(id = R.string.quiz_games) },
                onOpenDrawer = { scope.launch { parentScaffoldState.drawerState.open() } },
            )
        },
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
        ) {
            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.small)) }

            items(count = QuizGame.values().size) { i ->
                val quizGame = QuizGame.values()[i]
                Card(
                    onClick = {
                        navigator.navigate(
                            QuizPlayingScreenDestination(quizGameName = quizGame.name)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.small,
                        ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.medium)
                            .fillMaxWidth(),
                    ) {
                        Text(text = QuizGame.values()[i].title.asString())
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.small)) }
        }
    }
}