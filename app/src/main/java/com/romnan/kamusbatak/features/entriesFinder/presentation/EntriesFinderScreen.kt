package com.romnan.kamusbatak.features.entriesFinder.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.presentation.model.EntryParcelable
import com.romnan.kamusbatak.core.presentation.util.UIEvent
import com.romnan.kamusbatak.features.destinations.EntryDetailScreenDestination
import com.romnan.kamusbatak.features.destinations.PreferencesScreenDestination
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalAnimationApi::class)
@Destination(start = true)
@Composable
fun EntriesFinderScreen(
    navigator: DestinationsNavigator,
    viewModel: EntriesFinderViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    focusManager.clearFocus() // TODO: find another way to display this
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(48.dp))
                    AnimatedContent(
                        targetState = state.targetLanguage.fullName,
                        modifier = Modifier.weight(1f),
                        transitionSpec = {
                            slideIntoContainer(
                                towards = AnimatedContentScope.SlideDirection.Left
                            ) with slideOutOfContainer(
                                towards = AnimatedContentScope.SlideDirection.Right
                            )
                        }
                    ) {
                        Text(
                            text = state.sourceLanguage.fullName,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6
                        )
                    }
                    IconButton(onClick = {
                        viewModel.onEvent(EntriesFinderEvent.LanguagesSwap)
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SwapHoriz,
                            contentDescription = stringResource(id = R.string.swap_languages)
                        )
                    }
                    AnimatedContent(
                        targetState = state.targetLanguage.fullName,
                        modifier = Modifier.weight(1f),
                        transitionSpec = {
                            slideIntoContainer(
                                towards = AnimatedContentScope.SlideDirection.Right
                            ) with slideOutOfContainer(
                                towards = AnimatedContentScope.SlideDirection.Left
                            )
                        }
                    ) {
                        Text(
                            text = state.targetLanguage.fullName,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6
                        )
                    }

                    IconButton(onClick = {
                        viewModel.onEvent(EntriesFinderEvent.SetShowOptionsMenu(true))
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.options)
                        )
                    }
                }

                Box {
                    DropdownMenu(
                        expanded = state.isOptionsMenuShown,
                        onDismissRequest = {
                            viewModel.onEvent(EntriesFinderEvent.SetShowOptionsMenu(false))
                        }
                    ) {
                        DropdownMenuItem(onClick = {
                            viewModel.onEvent(EntriesFinderEvent.SetShowOptionsMenu(false))
                            navigator.navigate(PreferencesScreenDestination())
                        }) {
                            Text(text = stringResource(id = R.string.preferences))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val bottomCorRad by animateDpAsState(if (state.isLoadingEntries) 0.dp else 8.dp)
            TextField(
                singleLine = true,
                value = viewModel.searchQuery.value,
                placeholder = { Text(text = stringResource(R.string.enter_words_here)) },
                onValueChange = { viewModel.onEvent(EntriesFinderEvent.QueryChange(it)) },
                trailingIcon = {
                    Icon(Icons.Default.Search, stringResource(R.string.enter_words_here))
                },
                shape = RoundedCornerShape(8.dp, 8.dp, bottomCorRad, bottomCorRad),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            AnimatedVisibility(visible = state.isLoadingEntries) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(visible = state.entries.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.entries.size) { i ->
                        val entry = state.entries[i]

                        EntryItem(entry = entry, modifier = Modifier.clickable {
                            navigator.navigate(
                                EntryDetailScreenDestination(
                                    entry = EntryParcelable(entry)
                                )
                            )
                        })

                        if (i < state.entries.size - 1) Divider(
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}