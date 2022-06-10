package com.romnan.kamusbatak.featEntriesFinder.presentation

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.presentation.model.toParcelable
import com.romnan.kamusbatak.core.presentation.theme.spacing
import com.romnan.kamusbatak.core.presentation.util.UIEvent
import com.romnan.kamusbatak.core.presentation.util.asString
import com.romnan.kamusbatak.core.util.Constants
import com.romnan.kamusbatak.destinations.EntryDetailScreenDestination
import com.romnan.kamusbatak.destinations.PreferencesScreenDestination
import com.romnan.kamusbatak.featEntriesFinder.presentation.components.EntryItem
import com.romnan.kamusbatak.featEntriesFinder.presentation.components.NavigationDrawerItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun EntriesFinderScreen(
    navigator: DestinationsNavigator,
    viewModel: EntriesFinderViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

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
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxSize()
                    .padding(
                        top = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = MaterialTheme.spacing.medium)
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.h6,
                    )
                }

                Divider(
                    modifier = Modifier.padding(
                        start = MaterialTheme.spacing.medium,
                        top = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium,
                    )
                )

                NavigationDrawerItem(
                    icon = Icons.Default.Search,
                    label = stringResource(R.string.dictionary),
                    selected = true
                ) {
                    coroutineScope.launch { scaffoldState.drawerState.close() }
                }

                NavigationDrawerItem(
                    icon = Icons.Default.Settings,
                    label = stringResource(id = R.string.preferences)
                ) {
                    coroutineScope.launch { scaffoldState.drawerState.close() }
                    navigator.navigate(PreferencesScreenDestination)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            TopAppBar(backgroundColor = MaterialTheme.colors.surface) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.menu),
                            tint = MaterialTheme.colors.onSurface
                        )
                    }

                    AnimatedContent(
                        targetState = state.targetLanguage.displayName,
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
                            text = state.sourceLanguage.displayName.asString(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.onSurface
                        )
                    }

                    IconButton(
                        onClick = { viewModel.onEvent(EntriesFinderEvent.LanguagesSwap) },
                        modifier = Modifier
                            .background(color = MaterialTheme.colors.primary, shape = CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SwapHoriz,
                            contentDescription = stringResource(id = R.string.cd_swap_languages),
                            tint = MaterialTheme.colors.onPrimary,
                        )
                    }

                    AnimatedContent(
                        targetState = state.targetLanguage.displayName,
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
                            text = state.targetLanguage.displayName.asString(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.onSurface
                        )
                    }

                    IconButton(onClick = {
                        viewModel.onEvent(EntriesFinderEvent.SetShowOptionsMenu(true))
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.cd_options),
                            tint = MaterialTheme.colors.onSurface
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

                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = Constants.INTENT_TYPE_PLAIN_TEXT
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    context.getString(R.string.download_kamus_batak)
                                )
                            }

                            context.startActivity(intent)
                        }) {
                            Text(text = stringResource(R.string.share_this_app))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            val bottomCorRad by animateDpAsState(if (state.isLoadingEntries) 0.dp else 8.dp)
            TextField(
                singleLine = true,
                value = viewModel.searchQuery.value,
                placeholder = { Text(text = stringResource(R.string.ph_enter_words_here)) },
                onValueChange = { viewModel.onEvent(EntriesFinderEvent.QueryChange(it)) },
                trailingIcon = {
                    Icon(Icons.Default.Search, stringResource(R.string.ph_enter_words_here))
                },
                shape = RoundedCornerShape(8.dp, 8.dp, bottomCorRad, bottomCorRad),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = MaterialTheme.colors.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.small)
            )

            AnimatedVisibility(visible = state.isLoadingEntries) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.small)
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(viewModel.entries.value.size) { i ->
                    EntryItem(
                        entry = viewModel.entries.value[i],
                        onClick = {
                            navigator.navigate(EntryDetailScreenDestination(it.toParcelable()))
                        }
                    )
                }
            }
        }
    }
}