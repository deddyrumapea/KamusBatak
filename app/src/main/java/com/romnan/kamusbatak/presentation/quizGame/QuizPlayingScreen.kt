package com.romnan.kamusbatak.presentation.quizGame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.QuizGame
import com.romnan.kamusbatak.presentation.components.RoundedEndsButton
import com.romnan.kamusbatak.presentation.destinations.QuizGamesScreenDestination
import com.romnan.kamusbatak.presentation.destinations.QuizResultScreenDestination
import com.romnan.kamusbatak.presentation.quizGame.component.AnswerSheetContent
import com.romnan.kamusbatak.presentation.quizGame.component.QuizOptionItem
import com.romnan.kamusbatak.presentation.theme.spacing
import com.romnan.kamusbatak.presentation.util.UIEvent
import com.romnan.kamusbatak.presentation.util.asString
import com.romnan.kamusbatak.presentation.util.launchSendSuggestionIntent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination
fun QuizPlayingScreen(
    quizGameName: String = QuizGame.VocabMix.name,
    navigator: DestinationsNavigator,
    viewModel: QuizPlayingViewModel = hiltViewModel(),
) {
    remember { viewModel.onReceivedQuizGameName(value = quizGameName) }

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

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            AnswerSheetContent(
                isAnswerCorrect = { viewModel.isAnswerCorrect },
                correctAnswer = { viewModel.currQuizItem.correctOption ?: "" },
                onClickNext = {
                    scope.launch {
                        sheetState.hide()

                        viewModel.onClickNext()

                        if (viewModel.currQuizItem.number >= viewModel.quizItemsSize) navigator.navigate(
                            QuizResultScreenDestination(
                                quizGameName = viewModel.quizGameName,
                                totalItems = viewModel.quizItemsSize,
                                correctItems = viewModel.correctAnswersCount,
                            )
                        ) {
                            popUpTo(QuizGamesScreenDestination.route) { saveState = false }
                        }
                    }
                },
                onClickReport = {
                    launchSendSuggestionIntent(
                        context = context,
                        quizItem = viewModel.currQuizItem,
                    )
                },
            )
        },
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.primary,
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 0.dp,
                ) {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.cd_nav_back),
                            tint = contentColorFor(backgroundColor = MaterialTheme.colors.primary),
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.playing_quiz),
                        style = MaterialTheme.typography.h6,
                        color = contentColorFor(backgroundColor = MaterialTheme.colors.primary),
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            },
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max)
                                .padding(MaterialTheme.spacing.medium),
                        ) {
                            Card(shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = "${viewModel.currQuizItem.number}/${viewModel.quizItemsSize}",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                                )
                            }

                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                            Card(
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                            ) {
                                LinearProgressIndicator(
                                    progress = (viewModel.currQuizItem.number / viewModel.quizItemsSize.toFloat()),
                                    modifier = Modifier.fillMaxSize(),
                                    color = MaterialTheme.colors.secondary,
                                )
                            }
                        }

                        Card(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(MaterialTheme.spacing.medium),
                            ) {
                                Text(
                                    text = stringResource(R.string.quiz_direction),
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                                    textAlign = TextAlign.Center,
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                                Text(
                                    text = viewModel.currQuizItem.question,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.h6,
                                    textAlign = TextAlign.Center,
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                            }
                        }

                        Card(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.spacing.medium),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.spacing.small),
                            ) {
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                                val alphabet = ('A'..'Z').toList()
                                viewModel.currQuizItem.options.indices.forEach { i ->
                                    QuizOptionItem(
                                        char = { alphabet[i % alphabet.size] },
                                        text = { viewModel.currQuizItem.options[i] },
                                        isSelected = { i == viewModel.selectedOptionIdx },
                                        isCorrect = { i == viewModel.currQuizItem.correctOptionIdx },
                                        isShowingAnswer = { viewModel.isShowingAnswer },
                                        onClick = { viewModel.onClickOption(i) },
                                    )

                                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                                }
                            }
                        }
                    }
                }

                Divider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                        .padding(MaterialTheme.spacing.medium),
                ) {
                    RoundedEndsButton(
                        onClick = {
                            scope.launch {
                                viewModel.onClickCheck()
                                if (viewModel.isShowingAnswer) sheetState.show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = stringResource(R.string.check),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colors.onSecondary,
                        )
                    }
                }
            }
        }
    }
}