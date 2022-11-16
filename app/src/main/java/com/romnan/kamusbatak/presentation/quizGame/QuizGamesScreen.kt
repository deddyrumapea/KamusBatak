package com.romnan.kamusbatak.presentation.quizGame

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.QuizGame
import com.romnan.kamusbatak.presentation.components.DefaultTopBar
import com.romnan.kamusbatak.presentation.components.RoundedEndsButton
import com.romnan.kamusbatak.presentation.destinations.QuizPlayingScreenDestination
import com.romnan.kamusbatak.presentation.theme.spacing
import com.romnan.kamusbatak.presentation.util.UIEvent
import com.romnan.kamusbatak.presentation.util.asString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination
fun QuizGamesScreen(
    navigator: DestinationsNavigator,
    parentScaffoldState: ScaffoldState,
    viewModel: QuizGamesViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopBar(
                title = { stringResource(id = R.string.quiz_games) },
                onOpenDrawer = { scope.launch { parentScaffoldState.drawerState.open() } },
            )
        },
    ) { scaffoldPadding ->
        val isOfflineSupported by viewModel.isOfflineSupported.collectAsState()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
        ) {
            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }

            if (isOfflineSupported == false) item {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium)
                    ) {
                        Text(text = stringResource(R.string.em_dictionary_data_needed))

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        RoundedEndsButton(
                            onClick = { viewModel.onUpdateLocalDb() },
                            enabled = !viewModel.isUpdatingLocalDb,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Icon(
                                imageVector = if (viewModel.isUpdatingLocalDb) Icons.Default.Downloading
                                else Icons.Default.Download,
                                contentDescription = null,
                            )

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                            Text(
                                text = stringResource(
                                    if (viewModel.isUpdatingLocalDb) R.string.downloading_dictionary_data
                                    else R.string.download_dictionary_data
                                ),
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }

            if (isOfflineSupported == true) items(count = QuizGame.values().size) { i ->
                val quizGame = QuizGame.values()[i]
                Card(
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        navigator.navigate(
                            QuizPlayingScreenDestination(quizGameName = quizGame.name)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.medium + MaterialTheme.spacing.small)
                            .fillMaxWidth(),
                    ) {
                        Text(text = QuizGame.values()[i].title.asString())
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }

            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.small)) }
        }
    }
}