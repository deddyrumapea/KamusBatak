package com.romnan.kamusbatak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romnan.kamusbatak.feature_search_entries.presentation.EntryItem
import com.romnan.kamusbatak.feature_search_entries.presentation.SearchEntriesEvent
import com.romnan.kamusbatak.feature_search_entries.presentation.SearchEntriesViewModel
import com.romnan.kamusbatak.ui.theme.KamusBatakTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KamusBatakTheme {
                val viewModel: SearchEntriesViewModel = hiltViewModel()
                val state = viewModel.state.value
                val scaffoldState = rememberScaffoldState()

                LaunchedEffect(key1 = true) {
                    viewModel.eventFlow.collectLatest { event ->
                        when (event) {
                            is SearchEntriesViewModel.UIEvent.ShowSnackbar -> {
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
                                    viewModel.onEvent(SearchEntriesEvent.LanguagesSwap)
                                }
                                ) {
                                    Icon(
                                        painterResource(R.drawable.ic_baseline_swap_horiz_24),
                                        getString(R.string.swap_languages)
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
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val bottomCorRad by animateDpAsState(if (state.isLoading) 0.dp else 8.dp)
                        TextField(
                            singleLine = true,
                            value = viewModel.searchQuery.value,
                            placeholder = { Text(text = getString(R.string.enter_words_here)) },
                            onValueChange = { viewModel.onEvent(SearchEntriesEvent.QueryChange(it)) },
                            trailingIcon = {
                                Icon(Icons.Default.Search, getString(R.string.enter_words_here))
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

                        AnimatedVisibility(visible = state.isLoading) {
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
                                    if (i > 0) Spacer(modifier = Modifier.height(8.dp))
                                    EntryItem(entry = state.entries[i])
                                    Spacer(modifier = Modifier.height(8.dp))
                                    if (i < state.entries.size - 1) Divider(
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}